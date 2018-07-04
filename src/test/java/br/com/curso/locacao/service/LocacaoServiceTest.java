package br.com.curso.locacao.service;

import static br.com.curso.locacao.builders.FilmeBuilder.umFilme;
import static br.com.curso.locacao.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.com.curso.locacao.builders.UsuarioBuilder.umUsuario;
import static br.com.curso.locacao.util.DataUtils.isMesmaData;
import static br.com.curso.locacao.util.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.curso.locacao.builders.FilmeBuilder;
import br.com.curso.locacao.builders.LocacaoBuilder;
import br.com.curso.locacao.builders.UsuarioBuilder;
import br.com.curso.locacao.exceptions.FilmeSemEstoqueException;
import br.com.curso.locacao.exceptions.LocadoraException;
import br.com.curso.locacao.model.Filme;
import br.com.curso.locacao.model.Locacao;
import br.com.curso.locacao.model.Usuario;
import br.com.curso.locacao.repository.LocacaoRepository;
import br.com.curso.locacao.runners.ParallelRunner;
import br.com.curso.locacao.util.DataUtils;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {

	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoRepository repository;
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(4.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio!"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();

		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, null);
	}

	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora());

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(11.0));
	}

	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora());

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(13.0));
	}

	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora(),umFilme().agora(),umFilme().agora(),umFilme().agora(),umFilme().agora());

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora(), umFilme().agora(), umFilme().agora(), umFilme().agora(),
				umFilme().agora(), umFilme().agora());

		// acao
		Locacao resultado = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(14.0));
	}

	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		assertTrue(ehSegunda);
	}
	
	@Test
	public void verificarSeUsuarioPossuiCPFNegativado() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		when(spcService.verificaUsuarioNegativado(usuario)).thenReturn(true);
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuario negativado");
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void enviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Locacao locacao = LocacaoBuilder.umLocacao().comUsuario(usuario).agora();
		List<Locacao> locacoes = Arrays.asList(locacao);
		when(repository.obterLocacoesAtrasadas()).thenReturn(locacoes);
		
		//acao
		service.enviarNotificacoesParaLocacoesAtrasadas();

		//verificacao
		verify(emailService).enviarNotificacoes(usuario);
	}
	
}

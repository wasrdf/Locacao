package br.com.curso.locacao.service;

import static br.com.curso.locacao.util.DataUtils.isMesmaData;
import static br.com.curso.locacao.util.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runners.MethodSorters;

import br.com.curso.locacao.model.Filme;
import br.com.curso.locacao.model.Locacao;
import br.com.curso.locacao.model.Usuario;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocacaoServiceTest {

	// cenario
	LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Before
	public void setup() {
		/**
		 * O Before é o metódo init do junit.
		 */
		service = new LocacaoService();
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testeLocacao() throws Exception {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");		
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 10.0));
		
		// acao
		final Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao esperado(cenário) , obtido(), delta
		error.checkThat(locacao.getValor(), is(equalTo(10.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
		
	}

	/**
	 * FORMA ROBUSTA
	 */
	@Test
	public void testeLocacao_usuarioVazio() {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 10.0));
		Usuario usuario = new Usuario("Usuario 1");
		
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Usuário vazio!"));
		}
	}
	
	
	@Test
	public void testeLocacao_filmeSemEstoque() {
		// cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 10.0));		
		
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("filme sem estoque"));
		}
	}

	@Test
	public void testeLocacao_filmeVazio() {		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");

		// acao
		try {
			service.alugarFilme(usuario, null);
			fail();
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme vazio!"));
		}
	}
}

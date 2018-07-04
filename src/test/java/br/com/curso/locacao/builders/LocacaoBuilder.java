package br.com.curso.locacao.builders;

import static br.com.curso.locacao.builders.FilmeBuilder.umFilme;
import static br.com.curso.locacao.builders.UsuarioBuilder.umUsuario;

import java.util.Arrays;
import java.util.Date;

import br.com.curso.locacao.model.Filme;
import br.com.curso.locacao.model.Locacao;
import br.com.curso.locacao.model.Usuario;
import br.com.curso.locacao.util.DataUtils;

public class LocacaoBuilder {
	private Locacao elemento;

	private LocacaoBuilder() {
	}

	public static LocacaoBuilder umLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(LocacaoBuilder builder) {
		builder.elemento = new Locacao();
		Locacao elemento = builder.elemento;

		elemento.setUsuario(umUsuario().agora());
		elemento.setFilmes(Arrays.asList(umFilme().agora()));
		elemento.setDataLocacao(new Date());
		elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
		elemento.setValor(4.0);
	}

	public LocacaoBuilder comUsuario(Usuario param) {
		elemento.setUsuario(param);
		return this;
	}

	public LocacaoBuilder comListaFilmes(Filme... params) {
		elemento.setFilmes(Arrays.asList(params));
		return this;
	}

	public LocacaoBuilder comDataLocacao(Date param) {
		elemento.setDataLocacao(param);
		return this;
	}

	public LocacaoBuilder comDataRetorno(Date param) {
		elemento.setDataRetorno(param);
		return this;
	}

	public LocacaoBuilder comValor(Double param) {
		elemento.setValor(param);
		return this;
	}

	public Locacao agora() {
		return elemento;
	}
}

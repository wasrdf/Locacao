package br.com.curso.locacao.builders;

import br.com.curso.locacao.model.Filme;

public class FilmeBuilder {
	
	private Filme filme;

	public FilmeBuilder() {
		
	}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme = new Filme("Filme 2", 2, 4.0);
		return builder;
	}
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme = new Filme("Filme 2", 0, 4.0);
		return builder;
	}
	
	public Filme agora() {
		return filme;
	}
	
	
}

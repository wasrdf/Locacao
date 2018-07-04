package br.com.curso.locacao.builders;

import br.com.curso.locacao.model.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	public UsuarioBuilder() {
	}

	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario();
		builder.usuario.setNome("Usuario 1");
		return builder;
	}

	public Usuario agora() {
		return usuario;
	}
	
	public UsuarioBuilder comNome(String nome) {
		usuario.setNome(nome);
		return this;
	}
}

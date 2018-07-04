package br.com.curso.locacao.service;

import br.com.curso.locacao.model.Usuario;

public interface EmailService {
	
	void enviarNotificacoes(Usuario usuario);
	
}

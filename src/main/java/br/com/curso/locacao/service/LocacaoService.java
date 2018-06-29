package br.com.curso.locacao.service;

import static br.com.curso.locacao.util.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.curso.locacao.model.Filme;
import br.com.curso.locacao.model.Locacao;
import br.com.curso.locacao.model.Usuario;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws Exception   {
		
		
		Optional<Usuario> user = Optional.ofNullable(usuario);
		
		if(!user.isPresent()) {
			throw new Exception("Usuário vazio!");
		}
		
		
		if(filmes == null || filmes.isEmpty()) {
			throw new Exception("Filme vazio!");
		}
		
		
		for (Filme filme : filmes) {
			
			if(filme.getEstoque() == 0) {
				throw new Exception("filme sem estoque");
			}
			
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		double valorTotal = 0d;
		
		for (Filme filme : filmes) {
			valorTotal+= filme.getPrecoLocacao();
		}
		
		locacao.setValor(valorTotal);
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		
	}
}
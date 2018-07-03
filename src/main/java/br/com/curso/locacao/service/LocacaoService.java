package br.com.curso.locacao.service;

import static br.com.curso.locacao.util.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.curso.locacao.exceptions.FilmeSemEstoqueException;
import br.com.curso.locacao.exceptions.LocadoraException;
import br.com.curso.locacao.model.Filme;
import br.com.curso.locacao.model.Locacao;
import br.com.curso.locacao.model.Usuario;
import br.com.curso.locacao.util.DataUtils;

public class LocacaoService {

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

		Optional<Usuario> user = Optional.ofNullable(usuario);

		if (!user.isPresent()) {
			throw new LocadoraException("Usuário vazio!");
		}

		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio!");
		}

		for (Filme filme : filmes) {

			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}

		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		double valorTotal = 0d;

		for (int i = 0; i < filmes.size(); i++) {

			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
			case 2:
				valorFilme = valorFilme * 0.75;
				break;
			case 3:
				valorFilme = valorFilme * 0.5;
				break;
			case 4:
				valorFilme = valorFilme * 0.25;
				break;
			case 5:
				valorFilme = 0d;
				break;
			}
			valorTotal += valorFilme;
		}

		locacao.setValor(valorTotal);

		// Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);

		// Salvando a locacao...
		// TODO adicionar método para salvar

		return locacao;
	}

	public static void main(String[] args) {

	}
}
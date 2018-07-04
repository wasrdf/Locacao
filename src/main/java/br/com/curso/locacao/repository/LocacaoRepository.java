package br.com.curso.locacao.repository;

import java.util.List;

import br.com.curso.locacao.model.Locacao;

public interface LocacaoRepository {
	
	void salvar(Locacao locacao);

	List<Locacao> obterLocacoesAtrasadas();
	
}

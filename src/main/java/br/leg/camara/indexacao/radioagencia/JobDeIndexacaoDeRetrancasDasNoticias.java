package br.leg.camara.indexacao.radioagencia;

import br.leg.camara.indexacao.api.JobAgregadorDeDocumentos;

/**
 * Job que agrega todas as retrancas de radio, retornando isso em um único documento a ser indexado/atualizado
 */
public class JobDeIndexacaoDeRetrancasDasNoticias extends JobAgregadorDeDocumentos<JobDeListagemDeRetrancas> {

	@Override
	protected JobDeListagemDeRetrancas criarJobAlvo() {
		return new JobDeListagemDeRetrancas();
	}

	@Override
	protected String nomeDoCampoAgregado() {
		return JobDeListagemDeRetrancas.NOME_CAMPO_RETRANCA;
	}

	@Override
	protected String nomeDoCampoComResultadoDaAgregacao() {
		return "retrancas";
	}

	@Override
	public String nome() {
		return "retrancas-radioagencia";
	}

	@Override
	public String nomeDoIndice() {
		return JobDeIndexacaoDeRadioagencia.NOME_DO_INDICE;
	}

	@Override
	public long quantidadeDeDocumentos() {
		return getJobAlvo().quantidadeDeNoticiasComRetranca();
	}
}

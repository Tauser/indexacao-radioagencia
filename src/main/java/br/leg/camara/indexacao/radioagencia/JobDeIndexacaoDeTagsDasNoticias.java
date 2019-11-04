package br.leg.camara.indexacao.radioagencia;

import br.leg.camara.indexacao.api.JobAgregadorDeDocumentos;

/**
 * Job que agrega todas as tags de radio, retornando isso em um único documento a ser indexado/atualizado
 */
public class JobDeIndexacaoDeTagsDasNoticias extends JobAgregadorDeDocumentos<JobDeListagemDeTags> {

	@Override
	protected JobDeListagemDeTags criarJobAlvo() {
		return new JobDeListagemDeTags();
	}

	@Override
	protected String nomeDoCampoAgregado() {
		return JobDeListagemDeTags.NOME_CAMPO;
	}

	@Override
	protected String nomeDoCampoComResultadoDaAgregacao() {
		return "tags";
	}

	@Override
	public String nome() {
		return "tags-radioagencia";
	}

	@Override
	public String nomeDoIndice() {
		return JobDeIndexacaoDeRadioagencia.NOME_DO_INDICE;
	}

	@Override
	public long quantidadeDeDocumentos() {
		return getJobAlvo().quantidadeDeNoticiasComCampoAgregador();
	}
}
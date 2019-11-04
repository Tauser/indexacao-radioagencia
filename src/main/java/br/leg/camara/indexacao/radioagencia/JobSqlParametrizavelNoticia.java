package br.leg.camara.indexacao.radioagencia;

import br.leg.camara.indexacao.adaptadores.sql.JobDeIndexacaoSql;
import br.leg.camara.indexacao.api.ParametrosExecucao;

abstract class JobSqlParametrizavelNoticia extends JobDeIndexacaoSql {

	static final String PARAMETRO_ID_NOTICIA = "idNoticia";

	private String idNoticia;

	@Override
	protected void receberParametros(ParametrosExecucao parametros) {
		idNoticia = parametros.valorSimples(PARAMETRO_ID_NOTICIA, null);
	}

	@Override
	protected Object[] argumentosDoSqlQuantidadeDeDocumentos() {
		return seIdNoticiaInformado(new Object[] {idNoticia}, new Object[]{});
	}

	@Override
	protected Object[] argumentosDoSqlTodosDocumentos() {
		return argumentosDoSqlQuantidadeDeDocumentos();
	}

	<T> T seIdNoticiaInformado(T valorCasoIdExista, T valorCasoIdAusente) {
		return idNoticia != null ? valorCasoIdExista : valorCasoIdAusente;
	}

	@Override
	public void encerrar() {
		super.encerrar();
		idNoticia = null;
	}
}

package br.leg.camara.indexacao.radioagencia;

import java.util.Map;

import br.leg.camara.indexacao.radioagencia.util.Campos;

/**
 * Lista audios de radioagencia, que serão agregadas
 */
public class JobDeListagemDeMidias extends JobSqlParametrizavelNoticia {

	static final String NOME_CAMPO = "idAudio";

	@Override
	protected String sqlTodosDocumentos() {
		return criarSql("p.ID as id, m.meta_value as " + NOME_CAMPO,
				"order by id asc, " + NOME_CAMPO + " asc");
	}

	private String criarSql(String colunasSelect, String orderBy) {
		return "select " + colunasSelect +
				" from wp_posts p "+
				" inner join wp_postmeta m on m.post_id = p.id and m.meta_key = '"+Campos.CD_POSTAUDIO+"' " +
				" where p.post_status = 'publish' and p.post_type = 'radioagencia' "+
				seIdNoticiaInformado(" and p.ID = ? ", "") +
				(orderBy != null ? orderBy : "");
	}

	@Override
	protected String sqlQuantidadeDeDocumentos() {
		return criarSql("count(*)", null);
	}
	
	@Override
	protected void adaptarDocumentoQueSeraIndexado(Map<String, Object> documento) {
		documento.put(NOME_CAMPO, Integer.valueOf(String.valueOf(documento.get(NOME_CAMPO))));
    }

	long quantidadeDeNoticiasComAudio() {
		final String sql = criarSql("count(distinct p.ID)", null);
		return getJdbcTemplate().queryForObject(sql, Long.class, argumentosDoSqlQuantidadeDeDocumentos());
	}

	@Override
	public String nome() {
		return "listagem-audio";
	}

	@Override
	public String nomeDoIndice() {
		return null;
	}

	@Override
	protected String nomeJobPrefixoPropriedade() {
		return JobDeIndexacaoDeRadioagencia.NOME_PREFIXO_PROPRIEDADE;
	}
}

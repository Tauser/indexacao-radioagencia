package br.leg.camara.indexacao.radioagencia;

/**
 * Lista as retrancas das notícias, que serão agregadas em {@link JobDeIndexacaoDeRetrancasDasNoticias}
 */
public class JobDeListagemDeRetrancas extends JobSqlParametrizavelNoticia {

	static final String NOME_CAMPO_RETRANCA = "retranca";
	static private String taxonomia = "tema";

	@Override
	protected String sqlTodosDocumentos() {
		return criarSql("tr.object_id as id, t.name as " + NOME_CAMPO_RETRANCA,
				"order by id asc, " + NOME_CAMPO_RETRANCA + " asc");
	}

	private String criarSql(String colunasSelect, String orderBy) {
		return "select " + colunasSelect +
				" from wp_term_relationships tr "+
				" inner join wp_term_taxonomy tt on tr.term_taxonomy_id = tt.term_taxonomy_id " +
				" inner join wp_terms t on tt.term_id = t.term_id " +
				" where tt.taxonomy = '"+taxonomia+"'" +
				" and (SELECT ps.ID FROM wp_posts ps where ps.ID = tr.object_id and ps.post_type ='radioagencia' and ps.post_status = 'publish') "+
				seIdNoticiaInformado(" and tr.object_id = ? ", "") +
				(orderBy != null ? orderBy : "");
	}

	@Override
	protected String sqlQuantidadeDeDocumentos() {
		return criarSql("count(*)", null);
	}

	long quantidadeDeNoticiasComRetranca() {
		final String sql = criarSql("count(distinct tr.object_id)", null);
		return getJdbcTemplate().queryForObject(sql, Long.class, argumentosDoSqlQuantidadeDeDocumentos());
	}

	@Override
	public String nome() {
		return "listagem-retrancas";
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

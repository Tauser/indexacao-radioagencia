package br.leg.camara.indexacao.radioagencia;

import br.leg.camara.indexacao.radioagencia.util.Campos;

/**
 * Lista os programas das notícias, que serão agregadas em {@link JobDeIndexacaoDeProgramasDasNoticias}
 */
public class JobDeListagemDeProgramas extends JobSqlParametrizavelNoticia {

	static final String NOME_CAMPO_PROGRAMA = "programa";

	@Override
	protected String sqlTodosDocumentos() {
		return criarSql("p.ID, "
				+ "(select sp.post_title from wp_posts sp where sp.ID in (m.meta_value)) as " + NOME_CAMPO_PROGRAMA,
				"order by id asc, " + NOME_CAMPO_PROGRAMA + " asc");
	}

	private String criarSql(String colunasSelect, String orderBy) {
		return "select " + colunasSelect +
				" from wp_posts p  " +
				" inner join wp_postmeta m on (p.ID = m.post_id) " +
				" where p.post_type = 'radioagencia' and p.post_status = 'publish' "+
				" and m.meta_key = '"+Campos.CD_RELACIONADAS+"' " +
				" and m.meta_value in (select sp.ID from wp_posts sp where sp.post_type in ('radioagencia')) " +
				seIdNoticiaInformado(" and m.post_id = ? ", "") +
				(orderBy != null ? orderBy : "");
	}

	@Override
	protected String sqlQuantidadeDeDocumentos() {
		return criarSql("count(*)", null);
	} 

	long quantidadeDeNoticiasComPrograma() {
		final String sql = criarSql("count(distinct m.post_id)", null);
		return getJdbcTemplate().queryForObject(sql, Long.class, argumentosDoSqlQuantidadeDeDocumentos());
	}

	@Override
	public String nome() {
		return "listagem-programas";
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


package br.leg.camara.indexacao.radioagencia;

import java.util.Map;

import br.leg.camara.indexacao.radioagencia.util.Campos;

public class JobDeListagemDeDeputados extends JobSqlParametrizavelNoticia {
	static final String NOME_CAMPO_ID_DEPUTADO = "idDeputado";

	@Override
	protected String sqlTodosDocumentos() {
		return criarSql("m.post_id as id, m.meta_value " + NOME_CAMPO_ID_DEPUTADO,
				"order by id asc, " + NOME_CAMPO_ID_DEPUTADO + " asc");
	}

	private String criarSql(String colunasSelect, String orderBy) {
		return "select " + colunasSelect +
				" from wp_postmeta m " +
				" where m.meta_key = '"+Campos.CD_DEPUTADOS+"' "+
				" and (SELECT ps.ID FROM wp_posts ps where ps.ID = m.post_id and ps.post_type ='radioagencia' and ps.post_status = 'publish') "+
				seIdNoticiaInformado(" and m.post_id = ? ", "") +
				(orderBy != null ? orderBy : "");
	}

	@Override
	protected void adaptarDocumentoQueSeraIndexado(Map<String, Object> documento) {
		documento.put("idDeputado", Integer.valueOf(String.valueOf(documento.get("idDeputado"))));
    }

	@Override
	protected String sqlQuantidadeDeDocumentos() {
		return criarSql("count(*)", null);
	} 

	long quantidadeDeNoticiasComIdDeputado() {
		final String sql = criarSql("count(distinct m.post_id)", null);
		return getJdbcTemplate().queryForObject(sql, Long.class, argumentosDoSqlQuantidadeDeDocumentos());
	}
	
	@Override
	public String nome() {
		return "listagem-deputados";
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

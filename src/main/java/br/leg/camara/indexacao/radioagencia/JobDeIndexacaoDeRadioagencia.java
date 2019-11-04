package br.leg.camara.indexacao.radioagencia;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import br.leg.camara.indexacao.radioagencia.JobSqlParametrizavelNoticia;
import br.leg.camara.indexacao.radioagencia.util.Campos;

import com.github.ooxi.phparser.SerializedPhpParser;
import com.github.ooxi.phparser.SerializedPhpParserException;

public class JobDeIndexacaoDeRadioagencia extends JobSqlParametrizavelNoticia  {
	
	static final String NOME_PREFIXO_PROPRIEDADE = "camaranews";
	static final String NOME_DO_INDICE = "radioagencia";

	
	@Override
	protected String sqlTodosDocumentos() {
//		 return criarSql("n.ID as id, n.post_title as titulo, n.post_excerpt as resumo, "
//				 	+ "(SELECT ps.post_title FROM wp_posts ps where ps.ID = m.meta_value) as programa, "
//				 	+ "(SELECT m3.meta_value FROM wp_postmeta m3 where m3.post_id = m2.meta_value and m3.meta_key = '"+Campos.ATTACHMENT_METADATA+"') as tamanho, "
//		 			+ "concat_ws(IFNULL(n.post_content, ''),' ',IFNULL((SELECT m.meta_value FROM wp_postmeta m where m.meta_key = 'cd_rodape' and m.post_id = n.ID),'')) as materia, " +
//					"n.post_date as data, YEAR(n.post_date) as ano, 'radio-camara' as veiculo, 'radioagencia' as categoria,  n.post_date as dataOrdenacao, "
//					+ "(SELECT m.meta_value FROM wp_postmeta m where m.meta_key = '"+Campos.CD_RODAPE+"' and m.post_id = n.ID) as rodape ", 
//					"order by id asc");
		 
		 
		 return criarSql("n.ID as id, n.post_title as titulo, n.post_excerpt as resumo, "
				 	+ "(SELECT ps.post_title FROM wp_posts ps where ps.ID = m.meta_value) as programa, "
		 			+ "concat_ws(IFNULL(n.post_content, ''),' ',IFNULL((SELECT m.meta_value FROM wp_postmeta m where m.meta_key = 'cd_rodape' and m.post_id = n.ID),'')) as materia, " +
					"n.post_date as data, YEAR(n.post_date) as ano, 'radio-camara' as veiculo, 'radioagencia' as categoria, n.post_date as dataOrdenacao, "
					+ "(SELECT m.meta_value FROM wp_postmeta m where m.meta_key = '"+Campos.CD_RODAPE+"' and m.post_id = n.ID) as rodape ", 
					"order by id asc");
	}


	@Override
	protected String sqlQuantidadeDeDocumentos() {
		return criarSql("count(*)", null);
	}

	

	// Busca materias da agencia e materias da radio
		private String criarSql(String colunasSelect, String orderBy) {
//			return "select " + colunasSelect +
//					" from wp_posts n" +
//					" left join wp_postmeta m on (m.post_id = n.id and m.meta_key = '"+Campos.CD_PROGRAMA_PRINCIPAL+"')" +
//					" left join wp_postmeta m2 on (m2.post_id = n.id and m2.meta_key = '"+Campos.CD_POSTAUDIO+"')" +
//					" where n.post_type = 'radioagencia' " + 
//					" and n.post_status = 'publish' " +
//					seIdNoticiaInformado(" and n.ID = ? ", "") +
//					(orderBy != null ? orderBy : "");
			
			return "select " + colunasSelect +
					" from wp_posts n" +
					" left join wp_postmeta m on (m.post_id = n.id and m.meta_key = '"+Campos.CD_PROGRAMA_PRINCIPAL+"')" +
					" where n.post_type = 'radioagencia' " + 
					" and n.post_status = 'publish' " +
					seIdNoticiaInformado(" and n.ID = ? ", "") +
					(orderBy != null ? orderBy : "");
		}
	
	
	@Override
	protected void adaptarDocumentoQueSeraIndexado(Map<String, Object> documento) {
		//Retira as tags HTML do campo noticia
		String materia = (String)documento.get("materia");
		String materiaSemScripts = Jsoup.clean(materia, Whitelist.basic());
		String materiaSemTags = Jsoup.parse(materiaSemScripts).text();			
		documento.replace("materia", materiaSemTags);
		try {
			if(documento.get("tamanho") != null){
				Map<String, Object> dadosAudio = unserialized(documento.get("tamanho").toString());
				documento.replace("tamanho", dadosAudio.get("length_formatted"));	
			}			
		} catch (SerializedPhpParserException e) {
			e.printStackTrace();
			throw new IllegalStateException("Erro ao unserializar dados: ", e);
		}
	}

	@Override
	public String nome() {
		return "noticias-radioagencia";
	}
	
	@Override
	public String nomeDoIndice() {
		return NOME_DO_INDICE;
	}
	
	@Override
	protected String nomeJobPrefixoPropriedade() {
		return NOME_PREFIXO_PROPRIEDADE;
	}
	
	/**
	 * Unserialized de dados do WORDPRESS
	 * @param valorSerializado
	 * @return
	 * @throws SerializedPhpParserException 
	 */
	
	private Map<String, Object> unserialized(String valorSerializado) throws SerializedPhpParserException {
		SerializedPhpParser serializedPhpParser = new SerializedPhpParser(valorSerializado);
		@SuppressWarnings("unchecked")
		Map<String, Object> mapUnserialised = (Map<String, Object>) serializedPhpParser.parse();
		return mapUnserialised;	
	}
}
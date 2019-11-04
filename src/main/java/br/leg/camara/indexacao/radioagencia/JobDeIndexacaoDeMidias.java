package br.leg.camara.indexacao.radioagencia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.github.ooxi.phparser.SerializedPhpParserException;

import br.leg.camara.indexacao.api.Configuracoes;
import br.leg.camara.indexacao.api.JobAgregadorDeDocumentos;
import br.leg.camara.indexacao.api.ParametrosExecucao;
import br.leg.camara.indexacao.radioagencia.util.Campos;
import br.leg.camara.indexacao.radioagencia.util.Util;

/**
 * Job que agrega todas os audios de radio, retornando isso em um único
 * documento a ser indexado/atualizado
 */
public class JobDeIndexacaoDeMidias extends JobAgregadorDeDocumentos<JobDeListagemDeMidias> {

	private JdbcTemplate jdbcTemplate;
	private Util util = new Util();
	List<DTOMidiaNoticia> audiosWP;

	@Override
	public void configurar(Configuracoes configuracoes) {
		super.configurar(configuracoes);
		DataSource datasource = criarDatasource(configuracoes);
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	private DataSource criarDatasource(Configuracoes configuracoes) {
		String prefixoPropriedades = "jobs." + JobDeIndexacaoDeRadioagencia.NOME_PREFIXO_PROPRIEDADE + ".datasource.";
		return criarDatasource(configuracoes, prefixoPropriedades);
	}

	private DataSource criarDatasource(Configuracoes configuracoes, String prefixoPropriedades) {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(configuracoes.getPropriedadeObrigatoria(prefixoPropriedades + "url"));
		ds.setDriverClassName(configuracoes.getPropriedadeObrigatoria(prefixoPropriedades + "driver"));
		ds.setUsername(configuracoes.getPropriedadeObrigatoria(prefixoPropriedades + "usuario"));
		ds.setPassword(configuracoes.getPropriedadeObrigatoria(prefixoPropriedades + "senha"));
		return ds;
	}

	private String sqlTodosAudios() {
		// @formatter:off
		return "select distinct(r.id), r.post_title titulo, r.guid url, sm.meta_value meta, (select pm2.meta_value from wp_postmeta pm2 where pm2.post_id = r.id and pm2.meta_key = '"+Campos.CD_MIDIA_URLEXTERNA+"') as url2" 
				+ " from wp_posts p "
				+ " inner join wp_postmeta m on m.post_id = p.id and m.meta_key = '"+Campos.CD_POSTAUDIO+"' "
				+ " inner join wp_posts r on m.meta_value = r.id"
				+ " left join wp_postmeta sm on sm.meta_key = '"+Campos.ATTACHMENT_METADATA+"' and sm.post_id = r.id"
				+ " where p.post_status = 'publish' and p.post_type = 'radioagencia' ";
		// @formatter:on
	}

	@Override
	public void iniciar(ParametrosExecucao parametros) {
		super.iniciar(parametros);
		//audiosWP = jdbcTemplate.query(sqlTodosAudios(), new BeanPropertyRowMapper<DTOMidia>(DTOMidia.class));
		audiosWP = jdbcTemplate.query(sqlTodosAudios(), new RowMapper<DTOMidiaNoticia>() {

			@Override
			public DTOMidiaNoticia mapRow(ResultSet rs, int rowNum) throws SQLException {
				DTOMidiaNoticia dto = new DTOMidiaNoticia();
				
				dto.setId(rs.getInt("id"));
				dto.setMeta(rs.getString("meta"));
				dto.setTitulo(rs.getString("titulo"));
				
				String url = rs.getString("url");
				String url2 = rs.getString("url2");
				
				if (url == null || url == "" || url.endsWith("/")) {
					dto.setUrl(url2);
				} else {
					dto.setUrl(url);
				}
		
				return dto;
			}
			
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void adaptarDocumentoQueSeraIndexado(Map<String, Object> documento) {
		String nomeCampoNormalizado = normalizarNomeCampo(nomeDoCampoComResultadoDaAgregacao());
		List<Integer> idsAudios = (List<Integer>) documento.get(nomeCampoNormalizado);
		List<Map<String, String>> audios = new ArrayList<Map<String, String>>();
		
		// @formatter:off		
		idsAudios.forEach(id -> {
			audiosWP.forEach(audio -> {
				if (id.equals(audio.getId())) {
					Map<String, String> jsonImagem = new HashMap<>();
					jsonImagem.put(normalizarNomeCampo("url"), audio.getUrl());
					jsonImagem.put(normalizarNomeCampo("titulo"), audio.getTitulo());
					try {
						if(audio.getMeta() != null){
							jsonImagem.put(normalizarNomeCampo("tamanho"), util.unserializedWordpress(audio.getMeta().toString()).get("length_formatted").toString());	
						}
					} catch (SerializedPhpParserException e) {
						e.printStackTrace();
						throw new IllegalStateException("Erro ao unserializar meta dados: ", e);
					}
					audios.add(jsonImagem);
				}
			});
		});
		// @formatter:on
		documento.replace(nomeCampoNormalizado, audios);
	}

	@Override
	protected JobDeListagemDeMidias criarJobAlvo() {
		return new JobDeListagemDeMidias();
	}

	@Override
	protected String nomeDoCampoAgregado() {
		return JobDeListagemDeMidias.NOME_CAMPO;
	}

	@Override
	protected String nomeDoCampoComResultadoDaAgregacao() {
		return "audios";
	}

	@Override
	public String nome() {
		return "audios-radioagencia";
	}

	@Override
	public String nomeDoIndice() {
		return JobDeIndexacaoDeRadioagencia.NOME_DO_INDICE;
	}

	@Override
	public long quantidadeDeDocumentos() {
		return getJobAlvo().quantidadeDeNoticiasComAudio();
	}

	@Override
	public void encerrar() {
		super.encerrar();
		audiosWP.clear();
	}

}
package br.leg.camara.indexacao.radioagencia;

import static br.leg.camara.indexacao.radioagencia.JobDeIndexacaoDeRadioagencia.NOME_PREFIXO_PROPRIEDADE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import br.leg.camara.indexacao.api.Configuracoes;
import br.leg.camara.indexacao.api.JobAgregadorDeDocumentos;
import br.leg.camara.indexacao.api.ParametrosExecucao;
import br.leg.camara.indexacao.radioagencia.util.ConversorJsonNodeParaDeputado;

/**
 * Job que indexa todos os deputados de uma notícia, retornando isso em um único documento a ser indexado/atualizado
 */
public class JobDeIndexacaoDeDeputadosDasNoticias extends JobAgregadorDeDocumentos<JobDeListagemDeDeputados> {

	private RestTemplate template;
	private Map<Integer, Deputado> deputadosPorIdDeputado;
	private static final String URL_PATH = "/arquivos/deputados/json/deputados.json";
	
	@Override
	public void configurar(Configuracoes configuracoes) {
		super.configurar(configuracoes);
		this.template = configurarRestTemplate(configuracoes);
	}
	
	private RestTemplate configurarRestTemplate(Configuracoes configuracoes) {
		String prefixoPropriedades = "jobs." + NOME_PREFIXO_PROPRIEDADE + ".rest.";

		String urlBase = configuracoes.getPropriedadeObrigatoria(prefixoPropriedades + "url-base");
		String usuario = configuracoes.getPropriedade(prefixoPropriedades + "usuario", null);
		String senha = configuracoes.getPropriedade(prefixoPropriedades + "senha", null);
		
		// @formatter:off 
		RestTemplateBuilder builder = new RestTemplateBuilder()
				.setReadTimeout(10000)
				.setConnectTimeout(5000)
				.rootUri(urlBase)				
				.messageConverters();
		// @formatter:on		

		if (usuario != null && senha != null) {
			builder.basicAuthorization(usuario, senha);
		}

		return builder.build();
	}
	
	private String url() {
		return URL_PATH;
	}

	@Override
	public void iniciar(ParametrosExecucao parametros) {
		super.iniciar(parametros);
		
		List<Deputado> deputados = listarDeputados();
		deputadosPorIdDeputado = deputados.stream()
				.collect(toMap(Deputado::getId, identity()));
	}
	
	@Override
	public void encerrar() {
		super.encerrar();
		if (deputadosPorIdDeputado != null) {
			deputadosPorIdDeputado.clear();
		}
	}
	
	private List<Deputado> listarDeputados() {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		JsonNode response =  template.getForObject(url(), JsonNode.class, entity);
		return ConversorJsonNodeParaDeputado.converter(response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void adaptarDocumentoQueSeraIndexado(Map<String, Object> documento) {
		String nomeCampoNormalizado = normalizarNomeCampo(nomeDoCampoComResultadoDaAgregacao());
		List<Integer> idsDeputados= (List<Integer>) documento.get(nomeCampoNormalizado);

		// @formatter:off
		List<Map<String, String>> nomesDeputados = idsDeputados.stream()
				.map(id -> deputadosPorIdDeputado.get(id))
				.filter(Objects::nonNull)
				.map(deputado -> {
					Map<String, String> jsonDeputado = new HashMap<>();
					jsonDeputado.put(normalizarNomeCampo("nomeDeputado"), deputado.getNomeDeputado());
					return jsonDeputado;
				}).collect(toList());
		// @formatter:on
		
		documento.replace(nomeCampoNormalizado, nomesDeputados);
	}
	
	@Override
	protected JobDeListagemDeDeputados criarJobAlvo() {
		return new JobDeListagemDeDeputados();
	}

	@Override
	protected String nomeDoCampoComResultadoDaAgregacao() {
		return "deputados";
	}

	@Override
	public String nome() {
		return "deputados-radioagencia";
	}

	@Override
	public String nomeDoIndice() {
		return JobDeIndexacaoDeRadioagencia.NOME_DO_INDICE;
	}

	@Override
	public long quantidadeDeDocumentos() {
		return getJobAlvo().quantidadeDeNoticiasComIdDeputado();
	}
	
}
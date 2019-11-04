package br.leg.camara.indexacao.radioagencia;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import br.leg.camara.indexacao.api.Configuracoes;
import br.leg.camara.indexacao.api.ConfiguracoesMapa;
import br.leg.camara.indexacao.api.ParametrosExecucao;

public class JobDeIndexacaoDeDeputadosDasNoticiasTest extends TesteDeIntegracaoComBancoH2 {
	
	private static final String PREFIXO_ARQUIVO_H2 = "indexacao-deputados";
	
	@Rule
	public WireMockRule selegWireMockRule = new WireMockRule(options().dynamicPort());
	
	@BeforeClass
	public static void criarBanco() throws Exception {
		criarBancoDeDados(PREFIXO_ARQUIVO_H2);
	}

	@AfterClass
	public static void apagarBanco() throws IOException {
		removerArquivosTemporariosDoH2(PREFIXO_ARQUIVO_H2);
	}
	
	@Test
	public void configurarNaoDeveLancarExcecao() {
		criarJobConfigurado();
	}
	
	@Test
	public void indexacaoDeDeputadosPrimeiroDocumento() {
		JobDeIndexacaoDeDeputadosDasNoticias job = criarJobConfigurado();

		job.iniciar(ParametrosExecucao.vazio());
		
		Map<String, ?> primeiro = job.proximoDocumento();
		
		assertEquals(1, primeiro.get("ID"));
		List<Map<String, ?>> deputados = (List<Map<String, ?>>) primeiro.get("DEPUTADOS");
		Map<String, ?> deputado = deputados.get(0);
		assertEquals("WELINTON FAGUNDES", deputado.get("NOMEDEPUTADO"));
		job.encerrar();
	}
	
	private JobDeIndexacaoDeDeputadosDasNoticias criarJobConfigurado() {
		Configuracoes configuracoes = criarConfiguracoes("http://localhost:" + selegWireMockRule.port());
		JobDeIndexacaoDeDeputadosDasNoticias job = new JobDeIndexacaoDeDeputadosDasNoticias();
		job.setNomesDeCamposEmCaixaAlta(true);
		job.configurar(configuracoes);
		return job;
	}

	private Configuracoes criarConfiguracoes(String urlBase) {
		ConfiguracoesMapa configuracoes = criarConfiguracoesBancoMemoria(PREFIXO_ARQUIVO_H2);
		configuracoes.adicionarPropriedade("jobs.camaranews.rest.url-base", urlBase);
		return configuracoes;
	}
}

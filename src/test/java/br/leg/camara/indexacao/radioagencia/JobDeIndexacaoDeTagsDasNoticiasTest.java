package br.leg.camara.indexacao.radioagencia;

import static br.leg.camara.indexacao.radioagencia.JobSqlParametrizavelNoticia.PARAMETRO_ID_NOTICIA;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.leg.camara.indexacao.api.Configuracoes;
import br.leg.camara.indexacao.api.ParametrosExecucao;

public class JobDeIndexacaoDeTagsDasNoticiasTest extends TesteDeIntegracaoComBancoH2 {

	private static final String PREFIXO_ARQUIVO_H2 = "tags";

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

	private JobDeIndexacaoDeTagsDasNoticias criarJobConfigurado() {
		Configuracoes configuracoes = criarConfiguracoesBancoMemoria(PREFIXO_ARQUIVO_H2);
		JobDeIndexacaoDeTagsDasNoticias job = new JobDeIndexacaoDeTagsDasNoticias();
		job.setNomesDeCamposEmCaixaAlta(true);
		job.configurar(configuracoes);
		return job;
	}

	@Test
	public void quantidadeDeDocumentosDeveRetornarNumeroDeNoticiasComRetranca() {
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();
		assertEquals(4, job.quantidadeDeDocumentos());
	}

	@Test
	public void proximoDocumentoPrimeiraChamada() {
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();

		job.iniciar(ParametrosExecucao.vazio());
		Map<String, ?> primeiro = job.proximoDocumento();

		assertNotNull(primeiro);
		//como o H2 retorna o nome das colunas sempre em Maiúsculas, estamos usando os nomes em maiúsculas
		assertEquals(1, primeiro.get("ID"));
		assertEquals(Arrays.asList("Meio ambiente", "Saiba mais"), primeiro.get("TAGS"));
	}

	@Test
	public void proximoDocumentoSegundaChamada() {
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();

		job.iniciar(ParametrosExecucao.vazio());
		job.proximoDocumento();
		Map<String, ?> segundo = job.proximoDocumento();

		assertNotNull(segundo);
		//como o H2 retorna o nome das colunas sempre em Maiúsculas, estamos usando os nomes em maiúsculas
		assertEquals(2, segundo.get("ID"));
		assertEquals(singletonList("Saiba mais"), segundo.get("TAGS"));
	}

	@Test
	public void proximoDocumentoTerceiraChamada() {
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();

		job.iniciar(ParametrosExecucao.vazio());
		job.proximoDocumento();
		job.proximoDocumento();
		Map<String, ?> terceiro = job.proximoDocumento();

		assertNotNull(terceiro);
		//como o H2 retorna o nome das colunas sempre em Maiúsculas, estamos usando os nomes em maiúsculas
		assertEquals(3, terceiro.get("ID"));
		assertEquals(singletonList("quebra de decoro"), terceiro.get("TAGS"));
	}

	@Test
	public void execucaoParametrizadaDeveRetornarTagsDeUmaNoticiaSomente() {
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();
		ParametrosExecucao parametros = ParametrosExecucao.comValorSimples(PARAMETRO_ID_NOTICIA, "1");
		job.iniciar(parametros);

		assertEquals(1, job.quantidadeDeDocumentos());

		Map<String, ?> doc = job.proximoDocumento();
		assertEquals(1, doc.get("ID"));
		assertEquals(Arrays.asList("Meio ambiente", "Saiba mais"), doc.get("TAGS"));
	}

	@Test
	public void execucaoParametrizadaComIdInexistenteDeveRetornarZeroDocumentos() {
		ParametrosExecucao parametros = ParametrosExecucao.comValorSimples(PARAMETRO_ID_NOTICIA, "999");
		JobDeIndexacaoDeTagsDasNoticias job = criarJobConfigurado();
		job.iniciar(parametros);

		assertEquals(0, job.quantidadeDeDocumentos());
	}
}
package br.leg.camara.indexacao.radioagencia;

import static br.leg.camara.indexacao.radioagencia.JobSqlParametrizavelNoticia.PARAMETRO_ID_NOTICIA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.leg.camara.indexacao.api.Configuracoes;
import br.leg.camara.indexacao.api.ParametrosExecucao;

public class JobDeListagemDeDeputadosTest extends TesteDeIntegracaoComBancoH2 {

	private static final String PREFIXO_ARQUIVO_H2 = "deputados";
	private static final String CAMPO_ID_DEPUTADO = JobDeListagemDeDeputados.NOME_CAMPO_ID_DEPUTADO.toUpperCase();

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

	private JobDeListagemDeDeputados criarJobConfigurado() {
		Configuracoes configuracoes = criarConfiguracoesBancoMemoria(PREFIXO_ARQUIVO_H2);
		JobDeListagemDeDeputados job = new JobDeListagemDeDeputados();
		job.configurar(configuracoes);
		return job;
	}

	@Test
	public void quantidadeDeDocumentosDeveRetornarNumeroDeParesNoticiaDeputado() {
		JobDeListagemDeDeputados job = criarJobConfigurado();
		assertEquals(5, job.quantidadeDeDocumentos());
	}

	@Test
	public void proximoDocumentoPrimeiraChamada() {
		JobDeListagemDeDeputados job = criarJobConfigurado();

		job.iniciar(ParametrosExecucao.vazio());
		Map<String, ?> primeiro = job.proximoDocumento();

		assertNotNull(primeiro);
		//como o H2 retorna o nome das colunas sempre em Maiúsculas, estamos usando os nomes em maiúsculas
		assertEquals(1, primeiro.get("ID"));
		assertEquals(73653, primeiro.get(CAMPO_ID_DEPUTADO));
	}

	@Test
	public void execucaoParametrizadaDeveRetornarProgramasDeUmaNoticiaSomente() {
		JobDeListagemDeDeputados job = criarJobConfigurado();
		ParametrosExecucao parametros = ParametrosExecucao.comValorSimples(PARAMETRO_ID_NOTICIA, "2");
		job.iniciar(parametros);

		assertEquals(2, job.quantidadeDeDocumentos());

		//testa primeiro e último documento
		Map<String, ?> doc = job.proximoDocumento();
		
		assertEquals(2, doc.get("ID"));
		assertEquals(141436, doc.get(CAMPO_ID_DEPUTADO));
		
		doc = job.proximoDocumento();
		assertEquals(2, doc.get("ID"));
		assertEquals(74047, doc.get(CAMPO_ID_DEPUTADO));
		
	}
	
	@Test
	public void quantidadeDeNoticiasComDeputadoSoDeveContarUmaVezAMesmaNoticia() {
		JobDeListagemDeDeputados job = criarJobConfigurado();
		assertEquals(4, job.quantidadeDeNoticiasComIdDeputado());
	}
}
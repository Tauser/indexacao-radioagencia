package br.leg.camara.indexacao.radioagencia;

import java.io.IOException;

import org.h2.tools.DeleteDbFiles;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import br.leg.camara.indexacao.api.ConfiguracoesMapa;

abstract class TesteDeIntegracaoComBancoH2 {

	private static final String SCRIPT_SQL = "scriptTestes.sql";
	private static final String DATASOURCE_USER = "sa";
	private static final String DATASOURCE_PASS = "";

	private static final String DATABASE_DIRECTORY = "./target";
	
	static void criarBancoDeDados(String prefixoFileH2) throws Exception {
		removerArquivosTemporariosDoH2(prefixoFileH2);

		SingleConnectionDataSource ds = new SingleConnectionDataSource(datasourceUrl(prefixoFileH2), DATASOURCE_USER, DATASOURCE_PASS, true);
		System.out.println("Executando script " + SCRIPT_SQL);
		ScriptUtils.executeSqlScript(ds.getConnection(), new ClassPathResource(SCRIPT_SQL));
	}

	private static String h2FileName(String prefixo) {
		return "test-"+prefixo;
	}
	
	private static String h2FileNameWithPath(String prefixo) {
		return DATABASE_DIRECTORY+"/"+h2FileName(prefixo);
	}

	private static String datasourceUrl(String prefixoFileH2) {
		return "jdbc:h2:" + h2FileNameWithPath(prefixoFileH2) + ";MODE=MySQL";
	}
	static void removerArquivosTemporariosDoH2(String prefixoFileH2) throws IOException {
		String h2File = h2FileName(prefixoFileH2);
		System.out.println("Removendo arquivos do H2 " );
		DeleteDbFiles.execute(DATABASE_DIRECTORY, h2File, true);
	}

	ConfiguracoesMapa criarConfiguracoesBancoMemoria(String prefixoFileH2) {
		ConfiguracoesMapa config = new ConfiguracoesMapa();
		config.adicionarPropriedade("jobs.camaranews.datasource.url", datasourceUrl(prefixoFileH2));
		config.adicionarPropriedade("jobs.camaranews.datasource.driver", "org.h2.Driver");
		config.adicionarPropriedade("jobs.camaranews.datasource.usuario", DATASOURCE_USER);
		config.adicionarPropriedade("jobs.camaranews.datasource.senha", DATASOURCE_PASS);
		return config;
	}
}

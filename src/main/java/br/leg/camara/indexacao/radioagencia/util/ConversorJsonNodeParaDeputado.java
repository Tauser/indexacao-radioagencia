package br.leg.camara.indexacao.radioagencia.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import br.leg.camara.indexacao.radioagencia.Deputado;

public class ConversorJsonNodeParaDeputado {
	
	public static List<Deputado> converter(JsonNode json) {
		List<Deputado> retorno = new ArrayList<Deputado>();
		
		JsonNode listaDeputados = json.get("dados");
		for (JsonNode jsonDeputado : listaDeputados) {
			Integer idDeputado = recuperaIdDeputadoDaUrl(jsonDeputado.get("uri").textValue());
			Deputado deputado = Deputado.builder()
					.id(idDeputado)
					.nomeDeputado(jsonDeputado.get("nome").textValue())
					.build();
			
			retorno.add(deputado);
		}
		
		return retorno;
	}
	
	private static Integer recuperaIdDeputadoDaUrl(String url) {
		String partesUrl[] = (url).split("/");
		return  Integer.valueOf(partesUrl[partesUrl.length - 1]);
	}
}

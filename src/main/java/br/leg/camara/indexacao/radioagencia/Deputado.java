package br.leg.camara.indexacao.radioagencia;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Deputado {
	
	private final Integer id;
	private final String nomeDeputado;
}

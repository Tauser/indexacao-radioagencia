package br.leg.camara.indexacao.radioagencia.util;

import java.util.Map;

import com.github.ooxi.phparser.SerializedPhpParser;
import com.github.ooxi.phparser.SerializedPhpParserException;

public class Util {

	/**
	 * Unserialized de dados do WORDPRESS
	 * @param valorSerializado
	 * @return Map<String, Object>
	 * @throws SerializedPhpParserException 
	 */
	public Map<String, Object> unserializedWordpress(String valorSerializado) throws SerializedPhpParserException {
		SerializedPhpParser serializedPhpParser = new SerializedPhpParser(valorSerializado);
		@SuppressWarnings("unchecked")
		Map<String, Object> mapUnserialised = (Map<String, Object>) serializedPhpParser.parse();
		return mapUnserialised;	
	}
}

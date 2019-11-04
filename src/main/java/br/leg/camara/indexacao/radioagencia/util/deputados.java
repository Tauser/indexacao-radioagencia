package br.leg.camara.indexacao.radioagencia.util;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor

public class deputados {
	
	private final JdbcTemplate jdbcTemplateSileg;
    @Getter
    private Set<String> deputados = new HashSet<>();


	
	private void carregaNomesDeputados() {
        final String sqlDeputados = String.join("\n",
                "SELECT DISTINCT pl.nom_parlamentar as nomeDeputado ",
                "FROM Pessoa_Legislatura pl ",
                "WHERE pl.num_legislatura = (select l.num_legislatura from legislatura l where getDate() between l.dat_inicio and l.dat_fim) ",
                "AND pl.nom_parlamentar IS NOT NULL ",
                "ORDER BY pl.nom_parlamentar"
        );

        queryForList(sqlDeputados, "nomeDeputado", this.deputados);
    }

    private void queryForList(@NonNull String query, @NonNull String nomeColuna, Set<String> dicionario) {
        if (dicionario == null) dicionario = new HashSet<>();
        List<Map<String, Object>> lista = this.jdbcTemplateSileg.queryForList(query);
        for (Map<String, Object> registro : lista) {
            dicionario.add((String) registro.get(nomeColuna));
        }
    }


}

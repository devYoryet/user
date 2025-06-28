package com.zosh.configrations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class OracleSequenceGenerator {

    private final JdbcTemplate jdbcTemplate;

    public OracleSequenceGenerator(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createSequenceIfNotExists(String sequenceName, String tableName) {
        try {
            // Verificar si la secuencia existe
            String checkQuery = "SELECT COUNT(*) FROM USER_SEQUENCES WHERE SEQUENCE_NAME = ?";
            Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, sequenceName.toUpperCase());

            if (count == 0) {
                // Crear secuencia
                String createSeq = String.format(
                        "CREATE SEQUENCE %s START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE",
                        sequenceName);
                jdbcTemplate.execute(createSeq);

                // Obtener el máximo ID actual de la tabla si existe
                try {
                    String maxIdQuery = String.format("SELECT NVL(MAX(id), 0) + 1 FROM %s", tableName);
                    Long maxId = jdbcTemplate.queryForObject(maxIdQuery, Long.class);

                    if (maxId > 1) {
                        String alterSeq = String.format("ALTER SEQUENCE %s RESTART START WITH %d", sequenceName, maxId);
                        jdbcTemplate.execute(alterSeq);
                    }
                } catch (Exception e) {
                    // Tabla no existe aún, usar valor por defecto
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creando secuencia Oracle: " + sequenceName, e);
        }
    }
}
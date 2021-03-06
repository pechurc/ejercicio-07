package com.eiv.repositories;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.eiv.entities.ProvinciaEntity;

@Repository
public class ProvinciaRepository implements CrudRepository<ProvinciaEntity, Long> {

    private static final String SQL_FIND_BY_ID = "SELECT * FROM provincias WHERE id=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM provincias";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    
    private final RowMapper<ProvinciaEntity> rowMapper = (rs, row) -> {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        return new ProvinciaEntity(id, nombre);
    };
    
    @Autowired
    public ProvinciaRepository(DataSource dataSource) throws SQLException {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("provincias");       
    }
    
    @Override
    public Optional<ProvinciaEntity> findById(Long id) {
        
        try {
            SqlParameterValue paramId = new SqlParameterValue(Types.INTEGER, id);
        
            ProvinciaEntity provinciaEntity = jdbcTemplate.queryForObject(
                    SQL_FIND_BY_ID, rowMapper, paramId);
            
            return Optional.of(provinciaEntity);  
        } catch (DataAccessException e) {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public List<ProvinciaEntity> findAll() {

        List<ProvinciaEntity> resultados = jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
        
        return resultados;
    }

    @Override
    public void insert(ProvinciaEntity t) {
          
        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("id", t.getId());
        parameters.put("nombre", t.getNombre());

        simpleJdbcInsert.execute(parameters);
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }

    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
    }
}

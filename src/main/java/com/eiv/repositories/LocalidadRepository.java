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

import com.eiv.entities.LocalidadEntity;

@Repository
public class LocalidadRepository implements CrudRepository<LocalidadEntity, Long> {
    
    private static final String SQL_FIND_BY_ID = "SELECT * FROM localidades WHERE id=?";
    private static final String SQL_FIND_BY_PROVINCIA = "SELECT * FROM localidades "
            + "WHERE provinciaId=?";
    private static final String SQL_FIND_ALL = "SELECT * FROM localidades";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    
    @Autowired
    public LocalidadRepository(DataSource dataSource) throws SQLException {
        jdbcTemplate = new JdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("localidades");       
    }
    
    private final RowMapper<LocalidadEntity> rowMapper = (rs, row) -> {
        long id = rs.getLong("id");
        String nombre = rs.getString("nombre");
        long provinciaId = rs.getLong("provinciaId");
        return new LocalidadEntity(id, nombre, provinciaId);
    };
    
    @Override
    public Optional<LocalidadEntity> findById(Long id) {

        try {
            SqlParameterValue paramId = new SqlParameterValue(Types.INTEGER, id);
        
            LocalidadEntity localidadEntity = jdbcTemplate.queryForObject(
                    SQL_FIND_BY_ID, rowMapper, paramId);
            
            return Optional.of(localidadEntity);  
        } catch (DataAccessException e) {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public List<LocalidadEntity> findAll() {
        
        List<LocalidadEntity> resultados = jdbcTemplate.query(SQL_FIND_ALL, rowMapper);
        
        return resultados;
    }

    public List<LocalidadEntity> findAllByProvincia(Long provinciaId) {
        
        SqlParameterValue paramId = new SqlParameterValue(Types.INTEGER, provinciaId);
        
        List<LocalidadEntity> resultados = jdbcTemplate
                .query(SQL_FIND_BY_PROVINCIA, rowMapper, paramId);
        
        return resultados;
    }
    
    @Override
    public void insert(LocalidadEntity t) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        
        parameters.put("id", t.getId());
        parameters.put("nombre", t.getNombre());
        parameters.put("provinciaId", t.getProvinciaId());

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

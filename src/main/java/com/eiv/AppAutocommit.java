package com.eiv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public class AppAutocommit {
    
    private static final String SQL = "SELECT * FROM provincias";
    private static final String SQL_INSERT = "INSERT INTO provincias (id, nombre) VALUES (?, ?)";
    
    public static void main(String[] args) throws SQLException {
        org.h2.tools.Server.main();
        AppAutocommit app = new AppAutocommit();
        app.run();
    }
    
    public void run() {
        
        try (Connection conn = getDataSource().getConnection();
                PreparedStatement stmt = conn.prepareStatement(SQL_INSERT);) {
            conn.setAutoCommit(false);
            try {
                
                for (int i = 10; i < 13; i++) {
                    stmt.setInt(1, i);
                    stmt.setString(2, "Nombre-" + i);
                    
                    stmt.execute();
                }
                
                stmt.setInt(1, 120);
                stmt.setString(2, "Nombre-" + 12);
            
           
                stmt.execute();
            } catch (SQLException e) {
                conn.rollback();
            }
            
            conn.commit();
            
            ResultSet rs = conn.createStatement().executeQuery(SQL);
            
            while (rs.next()) {
                System.out.println("Provincia: " + rs.getString("nombre"));
            }
            
        } catch (SQLException e) {            
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public void run1() {
        
        try (Connection conn = getDataSource().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL);) {
            
            while (rs.next()) {
                System.out.println("Provincia: " + rs.getString("nombre"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    private DataSource getDataSource() {
        
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb;INIT=runscript from 'src/main/resources/import.sql'");
        dataSource.setUser("sa");
        return dataSource;
    }
}

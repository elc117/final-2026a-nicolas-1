package com.restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL;
    private static final String USER = "res_user";
    private static final String PASSWORD = "restaurante";

    static {

        // Carrega o driver do postgre na memoria
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException("PostgreSQL driver no found in project. Maybe check build.gradle.kts?");
        }

        // Checa a propriedade "environment" para decidir qual banco de dados utilizar (producao ou teste)
        String environment = System.getProperty("environment", "test");

        if(environment.equals("test")) {
            URL = "jdbc:postgresql://localhost:5432/restaurant_test_db";
        } else {
            URL = "jdbc:postgresql://localhost:5432/restaurant_db";
        }
    }


    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}

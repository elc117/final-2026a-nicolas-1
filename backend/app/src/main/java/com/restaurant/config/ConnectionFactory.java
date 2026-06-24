package com.restaurant.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        // Carrega o driver do postgre na memoria
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException("PostgreSQL driver not found in project. Maybe check build.gradle?");
        }

        // Checa a propriedade "environment" para decidir qual banco de dados utilizar (producao ou teste)
        // Padrao eh "production", mas ao rodar teste do gradle ele forca para "test"
        String environment = System.getProperty("environment", "production");   

        if(environment.equals("test")) {
            URL = "jdbc:postgresql://localhost:5432/restaurant_test_db";
            USER = "res_user";
            PASSWORD = "restaurante";
        } else {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            // Suporte para a variável padrão do Render (DATABASE_URL)
            String envDatabaseUrl = System.getenv("DATABASE_URL");
            if (envDatabaseUrl != null && !envDatabaseUrl.isEmpty()) {
                // Converte postgres:// ou postgresql:// para jdbc:postgresql://
                if (envDatabaseUrl.startsWith("postgres://")) {
                    dbUrl = "jdbc:postgresql://" + envDatabaseUrl.substring("postgres://".length());
                } else if (envDatabaseUrl.startsWith("postgresql://")) {
                    dbUrl = "jdbc:postgresql://" + envDatabaseUrl.substring("postgresql://".length());
                } else {
                    dbUrl = envDatabaseUrl;
                }
                // No caso do DATABASE_URL completo, usuário e senha já vêm embutidos na URL
                dbUser = null;
                dbPassword = null;
            }

            URL = dbUrl != null ? dbUrl : "jdbc:postgresql://localhost:5432/restaurant_db";
            USER = dbUrl != null ? dbUser : "res_user";
            PASSWORD = dbUrl != null ? dbPassword : "restaurante";
        }
    }


    public static Connection getConnection(){
        try{
            if (USER == null || PASSWORD == null) {
                return DriverManager.getConnection(URL);
            }
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }
}

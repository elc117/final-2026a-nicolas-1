package com.restaurant.config;

import java.net.URI;
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
                try {
                    // Garante que a URI comece com postgresql:// para que o java.net.URI analise corretamente
                    String uriString = envDatabaseUrl;
                    if (envDatabaseUrl.startsWith("postgres://")) {
                        uriString = "postgresql://" + envDatabaseUrl.substring("postgres://".length());
                    }
                    URI uri = new URI(uriString);
                    
                    String host = uri.getHost();
                    int port = uri.getPort();
                    String path = uri.getPath();
                    String query = uri.getQuery();
                    
                    dbUrl = "jdbc:postgresql://" + host + (port != -1 ? ":" + port : "") + path;
                    if (query != null && !query.isEmpty()) {
                        dbUrl += "?" + query;
                    }

                    if (uri.getUserInfo() != null) {
                        String[] userInfo = uri.getUserInfo().split(":");
                        dbUser = userInfo[0];
                        if (userInfo.length > 1) {
                            dbPassword = userInfo[1];
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse DATABASE_URL: " + envDatabaseUrl, e);
                }
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

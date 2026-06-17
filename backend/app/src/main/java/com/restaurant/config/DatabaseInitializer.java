package com.restaurant.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initializeDatabase() {
        
        try(InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql")) {

            if (is == null) {
                throw new RuntimeException("Could not find schema.sql on resource directory");
            }

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            try(Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement()) {
                    
                stmt.execute(sql);
                System.out.println("DB tables verified/created successfully");

            }

        } catch (Exception e) {
            throw new RuntimeException("Could not initialize DB tables", e);
        }
    }

}

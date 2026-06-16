package com.restaurant.config;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseIntegrationTest {
    
    @BeforeEach
    @AfterEach
    protected void clearDB() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("TRUNCATE TABLE ingredients, employees, users RESTART IDENTITY");
        }
    }

}

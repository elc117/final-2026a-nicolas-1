package com.restaurant.config;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseIntegrationTest {
    
    @BeforeAll
    static void generalSetup() {
        DatabaseInitializer.initializeDatabase();
    }

    @BeforeEach
    @AfterEach
    protected void clearDB() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("TRUNCATE TABLE ingredients, employees, users, orders RESTART IDENTITY");
        }
    }

}

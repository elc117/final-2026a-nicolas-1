package com.restaurant.repository;

import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.User;
import com.restaurant.model.enums.Role;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class UserRepositoryIT {
    private final UserRepository repository = new UserRepository();

    @BeforeEach
    @AfterEach
    void clearDB() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("TRUNCATE TABLE employees, users RESTART IDENTITY");
            }
    }


    @Test
    @DisplayName("Must add an user to DB")
    void addTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);

        repository.add(user);

        assertNotNull(user.getId(), "User ID should not be null");
        assertTrue(user.getId() > 0, "User ID should be greater than 0");
    }


    @Test
    @DisplayName("Must retrieve all users from DB")
    void searchAllTest() {
        User user1 = new User("nicolas", "123456", Role.ADMIN);
        User user2 = new User("davi123", "654321", Role.COOK);

        repository.add(user1);
        repository.add(user2);
        List<User> usersFound = repository.searchAll();

        assertEquals(usersFound.get(0).getLogin(), "nicolas");
        assertEquals(usersFound.get(0).getPassword(), "123456");
        assertEquals(usersFound.get(0).getAccessProfile(), Role.ADMIN);
        assertEquals(usersFound.get(1).getLogin(), "davi123");
        assertEquals(usersFound.get(1).getPassword(), "654321");
        assertEquals(usersFound.get(1).getAccessProfile(), Role.COOK);
    }


    @Test
    @DisplayName("Must retrieve user data by ID from DB")
    void searchByIdTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);

        repository.add(user);

        Optional<User> foundOptional = repository.searchById(user.getId());
        
        assertTrue(foundOptional.isPresent(), "Could not find user by ID in DB");
        User found = foundOptional.get();
        assertEquals("nicolas", found.getLogin());
        assertEquals("123456", found.getPassword());
        assertEquals(Role.ADMIN, user.getAccessProfile());
    }


    @Test
    @DisplayName("Retrieve user data from DB by login")
    void searchByLoginTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);

        repository.add(user);

        Optional<User> foundOptional = repository.searchByLogin(user.getLogin());

        assertTrue(foundOptional.isPresent(), "Could not find user by login in DB");
        User found = foundOptional.get();
        assertEquals("nicolas", found.getLogin());
        assertEquals("123456", found.getPassword());
        assertEquals(Role.ADMIN, user.getAccessProfile());
    }

    
    @Test
    @DisplayName("Must update user data in DB")
    void updateTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        repository.add(user);

        user.setLogin("nicolas123");
        user.setAccessProfile(Role.CHEF);
        repository.update(user);

        assertEquals("nicolas123", user.getLogin());
        assertEquals(Role.CHEF, user.getAccessProfile());
    }


    @Test
    @DisplayName("Must delete user data in DB")
    void deleteTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        repository.add(user);

        repository.delete(user.getId());

        Optional<User> deleted = repository.searchById(user.getId());
        assertFalse(deleted.isPresent(), "User still exists in DB after deletion");
    }
}

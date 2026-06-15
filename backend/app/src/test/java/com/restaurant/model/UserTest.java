package com.restaurant.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import com.restaurant.model.enums.Role;

public class UserTest {
    
    @Test
    @DisplayName("Could not create new user successfully")
    void createUserTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);

        assertEquals("nicolas", user.getLogin());
        assertEquals("123456", user.getPassword());
        assertEquals(Role.ADMIN, user.getAccessProfile());
    }
    
}

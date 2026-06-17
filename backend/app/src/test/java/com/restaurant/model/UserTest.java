package com.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.model.enums.AccessProfile;

public class UserTest {
    
    @Test
    @DisplayName("Create user object")
    void createUserTest() {
        User user = new User(Long.valueOf(1), "nicolas", "123456", AccessProfile.ADMIN);

        assertEquals(1, user.getId());
        assertEquals("nicolas", user.getLogin());
        assertEquals("123456", user.getPassword());
        assertEquals(AccessProfile.ADMIN, user.getAccessProfile());
    }
    
}

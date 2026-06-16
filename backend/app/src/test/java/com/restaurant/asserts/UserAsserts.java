package com.restaurant.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restaurant.model.User;

public class UserAsserts {
    
    public static void assertValidId(User user) {
        assertNotNull(user.getId(), "Ingredient ID should not be null");
        assertTrue(user.getId() > 0, "Ingredient ID must be greater than 0");
    }

    public static void assertEqualUsers(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getAccessProfile(), user2.getAccessProfile());
    }
}

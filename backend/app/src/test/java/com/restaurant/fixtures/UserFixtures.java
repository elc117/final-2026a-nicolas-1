package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.User;
import com.restaurant.model.enums.AccessProfile;

public class UserFixtures {

    // IDs sao setados como 0, mas serao descartados e substituídos pelo ID definitivo definido pelo DB
    public static User createTestUser() {
        return new User(Long.valueOf(0), "marcelo", "marcelo123", AccessProfile.EMPLOYEE);
    }

    public static List<User> createTestUsers() {
        List<User> users = new ArrayList<>();
        
        users.add(new User(Long.valueOf(0), "marcelo", "marcelo123", AccessProfile.ADMIN));
        users.add(new User(Long.valueOf(0), "alberto", "alberto123", AccessProfile.CHEF));
        users.add(new User(Long.valueOf(0), "davi", "davi321", AccessProfile.EMPLOYEE));
        users.add(new User(Long.valueOf(0), "bernardo", "bernardo231", AccessProfile.EMPLOYEE));

        return users;
    }
}

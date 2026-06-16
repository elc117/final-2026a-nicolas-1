package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.User;
import com.restaurant.model.enums.AccessProfile;

public class UserFixtures {

    public static User createTestUser() {
        return new User("marcelo", "marcelo123", AccessProfile.EMPLOYEE);
    }

    public static List<User> createTestUsers() {
        List<User> users = new ArrayList<>();
        
        users.add(new User("marcelo", "marcelo123", AccessProfile.ADMIN));
        users.add(new User("alberto", "alberto123", AccessProfile.CHEF));
        users.add(new User("davi", "davi321", AccessProfile.EMPLOYEE));
        users.add(new User("bernardo", "bernardo231", AccessProfile.EMPLOYEE));

        return users;
    }
}

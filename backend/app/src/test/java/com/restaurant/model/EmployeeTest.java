package com.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.model.enums.AccessProfile;
import com.restaurant.model.enums.Role;

public class EmployeeTest {
    @Test
    @DisplayName("Create employee object")
    void createEmployee(){
        User user = new User(Long.valueOf(1), "marcelo", "marcelo123", AccessProfile.EMPLOYEE);
        Employee func1 = new Employee(Long.valueOf(1), "Marcelo", "Moraes", "12345678910", Role.COOK, user);

        assertEquals(1, func1.getId());
        assertEquals("Marcelo", func1.getName());
        assertEquals("12345678910", func1.getCpf());
        assertEquals(Role.COOK, func1.getRole());
    }
}

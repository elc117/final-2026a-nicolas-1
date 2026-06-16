package com.restaurant.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restaurant.model.Employee;

public class EmployeeAsserts {
    
    public static void assertValidId(Employee employee) {
        assertNotNull(employee.getId(), "Ingredient ID should not be null");
        assertTrue(employee.getId() > 0, "Ingredient ID must be greater than 0");
    }

    public static void assertEqualEmployees(Employee emp1, Employee emp2) {
        assertEquals(emp1.getName(), emp2.getName());
        assertEquals(emp1.getSurname(), emp2.getSurname());
        assertEquals(emp1.getCpf(), emp2.getCpf());
        assertEquals(emp1.getRole(), emp2.getRole());
        UserAsserts.assertEqualUsers(emp1.getUser(), emp2.getUser());
    }
}

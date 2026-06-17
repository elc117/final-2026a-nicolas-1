package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.Employee;
import com.restaurant.model.User;
import com.restaurant.model.enums.Role;


public class EmployeeFixtures {
    
    // IDs sao setados como 0, mas serao descartados e substituídos pelo ID definitivo definido pelo DB
    public static Employee createTestEmployee() {
        return new Employee(Long.valueOf(0), "Marcelo", "Nunes", "12345678910", Role.COOK, UserFixtures.createTestUser());
    }

    public static List<Employee> createTestEmployees() {
        List<User> users = UserFixtures.createTestUsers();
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(Long.valueOf(0), "Marcelo", "Nunes", "12345678910", Role.ADMIN, users.get(0)));
        employees.add(new Employee(Long.valueOf(0), "Alberto", "Paiani", "67676767676", Role.CHEF, users.get(1)));
        employees.add(new Employee(Long.valueOf(0), "Davi", "Barbosa", "01987654321", Role.WAITER, users.get(2)));
        employees.add(new Employee(Long.valueOf(0), "Bernardo", "Alves", "32165498709", Role.CASHIER, users.get(3)));
        
        return employees;
    }
    
}

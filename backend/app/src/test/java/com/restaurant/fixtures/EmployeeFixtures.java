package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.Employee;
import com.restaurant.model.User;


public class EmployeeFixtures {
    
    // IDs sao setados como 0, mas serao descartados e substituídos pelo ID definitivo definido pelo DB
    public static Employee createTestEmployee() {
        User user = UserFixtures.createTestUser();
        return new Employee(Long.valueOf(0), "Marcelo", "Nunes", "12345678910", "Cook", user != null, user);
    }

    public static List<Employee> createTestEmployees() {
        List<User> users = UserFixtures.createTestUsers();
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(Long.valueOf(0), "Marcelo", "Nunes", "12345678910", "Admin", users.get(0) != null, users.get(0)));
        employees.add(new Employee(Long.valueOf(0), "Alberto", "Paiani", "67676767676", "Chef", users.get(1) != null, users.get(1)));
        employees.add(new Employee(Long.valueOf(0), "Davi", "Barbosa", "01987654321", "Waiter", users.get(2) != null, users.get(2)));
        employees.add(new Employee(Long.valueOf(0), "Bernardo", "Alves", "32165498709", "Cashier", users.get(3) != null, users.get(3)));
        
        return employees;
    }
    
}

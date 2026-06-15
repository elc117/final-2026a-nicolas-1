package com.restaurant.repository;

import com.restaurant.model.enums.Role;
import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.Employee;
import com.restaurant.model.User;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class EmployeeRepositoryIT {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final UserRepository userRepository = new UserRepository();

    @BeforeEach
    @AfterEach
    void clearDB() throws Exception{
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("TRUNCATE TABLE employees, users RESTART IDENTITY");
        }
    }

    @Test
    @DisplayName("Add an employee to DB")
    void addTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        Employee employee = new Employee("Nicolas", "Atkinson", "12345678910", Role.ADMIN, user);

        userRepository.add(user);
        employeeRepository.add(employee);

        assertNotNull(user.getId(), "User ID should not be NULL");
        assertNotNull(employee.getId(), "Employee ID should not be NULL");
        assertTrue(user.getId() > 0, "User ID should be greater than 0");
        assertTrue(employee.getId() > 0, "Employee ID should be greater than 0");
    }


    @Test
    @DisplayName("Retrieve all employees data from DB")
    void searchAllTest() {
        User user1 = new User("nicolas", "123456", Role.ADMIN);
        Employee employee1 = new Employee("Nicolas", "Atkinson", "12345678910", Role.ADMIN, user1);

        User user2 = new User("davi123", "654321", Role.COOK);
        Employee employee2 = new Employee("Davi", "Libardoni", "01987654321", Role.COOK, user2);

        userRepository.add(user1);
        userRepository.add(user2);
        employeeRepository.add(employee1);
        employeeRepository.add(employee2);

        List<Employee> employeesFound = employeeRepository.searchAll();

        assertEquals(employeesFound.get(0).getId(), employee1.getId());
        assertEquals(employeesFound.get(0).getName(), employee1.getName());
        assertEquals(employeesFound.get(0).getUser().getId(), employee1.getUser().getId());
        assertEquals(employeesFound.get(1).getId(), employee2.getId());
        assertEquals(employeesFound.get(1).getName(), employee2.getName());
        assertEquals(employeesFound.get(1).getUser().getId(), employee2.getUser().getId());
    }


    @Test
    @DisplayName("Retrieve employee data from DB by ID")
    void searchByIdTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        Employee employee = new Employee("Nicolas", "Atkinson", "12345678910", Role.ADMIN, user);

        userRepository.add(user);
        employeeRepository.add(employee);

        Optional<Employee> foundOptional = employeeRepository.searchById(employee.getId());

        assertTrue(foundOptional.isPresent(), "Could not find employee in DB");
        Employee found = foundOptional.get();
        assertEquals("Nicolas", found.getName());
        assertEquals(Role.ADMIN, found.getRole());
        assertEquals("12345678910", found.getCpf());
        assertEquals("nicolas", found.getUser().getLogin());
        assertEquals(Role.ADMIN, found.getUser().getAccessProfile());
    }


    @Test
    @DisplayName("Update an employee ind DB")
    void updateTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        Employee employee = new Employee("Nicolas", "Atkinson", "12345678910", Role.ADMIN, user);

        userRepository.add(user);
        employeeRepository.add(employee);

        User newUser = new User("nicolas", "654321", Role.ADMIN);
        employee.setUser(newUser);
        employee.setSurname("Stroher");
        employee.setRole(Role.WAITER);
        employeeRepository.update(employee);

        Employee updated = employeeRepository.searchById(employee.getId()).get();

        assertEquals("654321", updated.getUser().getPassword());
        assertEquals("Stroher", updated.getSurname());
        assertEquals(Role.WAITER, updated.getRole());
    }


    @Test
    @DisplayName("Delete an employee from DB")
    void deleteTest() {
        User user = new User("nicolas", "123456", Role.ADMIN);
        Employee employee = new Employee("Nicolas", "Atkinson", "12345678910", Role.ADMIN, user);

        userRepository.add(user);
        employeeRepository.add(employee);

        employeeRepository.delete(employee.getId());

        Optional<Employee> deleted = employeeRepository.searchById(employee.getId());
        assertFalse(deleted.isPresent(), "Employee still exists in DB");
    }
}

package com.restaurant.repository;

import static com.restaurant.asserts.EmployeeAsserts.assertEqualEmployees;
import static com.restaurant.asserts.EmployeeAsserts.assertValidId;
import static com.restaurant.asserts.UserAsserts.assertValidId;
import static com.restaurant.fixtures.EmployeeFixtures.createTestEmployee;
import static com.restaurant.fixtures.EmployeeFixtures.createTestEmployees;
import static com.restaurant.fixtures.UserFixtures.createTestUser;
import static com.restaurant.fixtures.UserFixtures.createTestUsers;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.config.BaseIntegrationTest;
import com.restaurant.model.Employee;
import com.restaurant.model.User;
import com.restaurant.model.enums.Role;

public class EmployeeRepositoryIT extends BaseIntegrationTest{
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final UserRepository userRepository = new UserRepository();

    @Test
    @DisplayName("Add an employee to DB")
    void addTest() {
        User testUser = createTestUser();
        Employee testEmployee = createTestEmployee();

        userRepository.save(testUser);
        employeeRepository.save(testEmployee);

        assertValidId(testUser);
        assertValidId(testEmployee);
    }


    @Test
    @DisplayName("Retrieve all employees' data from DB")
    void searchAllTest() {
        List<User> testUsers = createTestUsers();
        List<Employee> testEmployees = createTestEmployees();

        userRepository.save(testUsers.get(0));
        userRepository.save(testUsers.get(1));
       
        testEmployees.get(0).setUser(testUsers.get(0));
        testEmployees.get(1).setUser(testUsers.get(1));
        
        employeeRepository.save(testEmployees.get(0));
        employeeRepository.save(testEmployees.get(1));

        List<Employee> employeesFound = employeeRepository.searchAll();

        assertEqualEmployees(testEmployees.get(0), employeesFound.get(0));
        assertEqualEmployees(testEmployees.get(1), employeesFound.get(1));
    }


    @Test
    @DisplayName("Retrieve an employee's data from DB by ID")
    void searchByIdTest() {
        User testUser = createTestUser();
        Employee testEmployee = createTestEmployee();

        userRepository.save(testUser);
       
        testEmployee.setUser(testUser);
        
        employeeRepository.save(testEmployee);

        Optional<Employee> foundOptional = employeeRepository.searchById(testEmployee.getId());

        assertTrue(foundOptional.isPresent(), "Could not find employee in DB");
        Employee found = foundOptional.get();
        assertEqualEmployees(testEmployee, found);
    }


    @Test
    @DisplayName("Update an employee's data in DB")
    void updateTest() {
        User testUser = createTestUser();
        Employee testEmployee = createTestEmployee();

        userRepository.save(testUser);
        employeeRepository.save(testEmployee);

        User newUser = createTestUsers().get(1);
        userRepository.save(newUser);

        testEmployee.setUser(newUser);
        testEmployee.setSurname("Stroher");
        testEmployee.setRole(Role.WAITER);

        employeeRepository.update(testEmployee);

        Employee updated = employeeRepository.searchById(testEmployee.getId()).get();

        assertEqualEmployees(testEmployee, updated);
    }


    @Test
    @DisplayName("Delete an employee from DB")
    void deleteTest() {
        User testUser = createTestUser();
        Employee testEmployee = createTestEmployee();

        userRepository.save(testUser);
        employeeRepository.save(testEmployee);

        employeeRepository.delete(testEmployee.getId());

        Optional<Employee> deleted = employeeRepository.searchById(testEmployee.getId());
        assertFalse(deleted.isPresent(), "Employee still exists in DB");
    }
}

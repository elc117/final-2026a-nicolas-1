package com.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.restaurant.dto.EmployeeDTO;
import com.restaurant.model.Employee;
import com.restaurant.model.User;
import com.restaurant.repository.EmployeeRepository;
import com.restaurant.utils.CpfUtils;

public class EmployeeService {
    
    private static final UserService userService = new UserService();
    private static final EmployeeRepository employeeRepository = new EmployeeRepository();

    public Employee registerEmployee(EmployeeDTO dto) {
        validateNewEmployee(dto);
        User user;

        if(dto.hasAccess()){
            char[] password = dto.user().getPassword().toCharArray();
            user = userService.registerUser(dto.user().toDto(), password);
        } else {
            user = null;
        }

        dto = new EmployeeDTO(
            dto.id(),
            dto.name(),
            dto.surname(),
            dto.cpf(),
            dto.role(),
            dto.hasAccess(),
            user
        );

        return employeeRepository.save(dto);
    }

    
    public List<Employee> listEmployees() {
        return employeeRepository.searchAll();
    }


    public Employee getEmployeeById(Long id) {
        checkValidId(id);
        return employeeRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("Employee with ID " + id + " does not exist"));
    }


    public Employee updateEmployee(EmployeeDTO dto) {
        checkValidEmployee(dto);

        Employee old = employeeRepository.searchById(dto.id()).get();

        if(!old.hasAccess() && dto.hasAccess()) {
            userService.registerUser(dto.user().toDto(), dto.user().getPassword().toCharArray());
        }

        if(old.hasAccess() && !dto.hasAccess()) {
            userService.deleteUser(old.getUser().getId());
        }

        return employeeRepository.update(dto);
    }


    public void deleteEmployee(Long id) {
        checkValidId(id);
        employeeRepository.delete(id);
    }


    
    // Funcoes auxiliares
    private static void checkValidId(Long id) {
        if(id <= 0 || id == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    private void checkValidEmployee(EmployeeDTO dto) {
        if(dto.name() == null || dto.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name is required");
        }

        if(dto.surname() == null || dto.surname().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee surname is required");
        }

        if(!CpfUtils.isValid(dto.cpf())) {
            throw new IllegalArgumentException("CPF must be valid");
        }
    }


    private void validateNewEmployee(EmployeeDTO dto) {
        checkValidEmployee(dto);

        if(employeeRepository.searchByCpf(dto.cpf()).isPresent()) {
            throw new IllegalArgumentException("Employee with this CPF already exists");
        }

        
    }
}

package com.restaurant.service;

import com.restaurant.model.Employee;
import com.restaurant.repository.EmployeeRepository;
import com.restaurant.utils.CpfUtils;

public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeService() {
        this.employeeRepository = new EmployeeRepository();
    }

    // Construtor alternativo para testes (Mock repository)
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    void registerEmployee(Employee employee) {
        validateNewEmployee(employee);
        employeeRepository.save(employee.toDto());
    }

    void validateNewEmployee(Employee employee) {
        if(employee.getId() != null) {
            throw new IllegalArgumentException("ID is defined by database, should be null");
        }

        if(employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name is required");
        }

        if(employee.getSurname() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee surname is required");
        }

        if(!CpfUtils.isValid(employee.getCpf())) {
            throw new IllegalArgumentException("CPF must be valid");
        }
    }
}

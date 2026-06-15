package com.restaurant.service;

import com.restaurant.repository.EmployeeRepository;

public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    public EmployeeService() {
        this.employeeRepository = new EmployeeRepository();
    }

    // Construtor alternativo para testes (Mock repository)
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

}

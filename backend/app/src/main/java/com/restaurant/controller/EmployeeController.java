package com.restaurant.controller;

import java.util.List;
import java.util.Map;

import com.restaurant.dto.EmployeeDTO;
import com.restaurant.model.Employee;
import com.restaurant.service.EmployeeService;

import io.javalin.http.Context;

public class EmployeeController {
    private final EmployeeService employeeService = new EmployeeService();

    public void register(Context ctx) {
        EmployeeDTO dto = ctx.bodyAsClass(EmployeeDTO.class);

        Employee newEmployee = employeeService.registerEmployee(dto);

        ctx.json(newEmployee);
        ctx.status(201);
    }


    public void list(Context ctx) {
        List<Employee> employees = employeeService.listEmployees();

        ctx.json(employees);
        ctx.status(200);
    }


    public void getById(Context ctx) {
        EmployeeDTO dto = ctx.bodyAsClass(EmployeeDTO.class);

        Employee employee = employeeService.getEmployeeById(dto.id());

        ctx.json(employee);
        ctx.status(200);
    }


    public void update(Context ctx) {
        EmployeeDTO dto = ctx.bodyAsClass(EmployeeDTO.class);

        Employee updatedEmployee = employeeService.updateEmployee(dto);

        ctx.json(updatedEmployee);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        EmployeeDTO dto = ctx.bodyAsClass(EmployeeDTO.class);

        employeeService.deleteEmployee(dto.id());

        ctx.json(Map.of());
        ctx.status(200);
    }
    
}

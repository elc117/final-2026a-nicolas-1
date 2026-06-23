package com.restaurant.controller;

import java.util.List;

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
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        Employee employee = employeeService.getEmployeeById(id);

        ctx.json(employee);
        ctx.status(200);
    }


    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        EmployeeDTO body = ctx.bodyAsClass(EmployeeDTO.class);

        EmployeeDTO toUpdate = new EmployeeDTO(
            id,
            body.name(), 
            body.surname(), 
            body.cpf(), 
            body.role(),
            body.hasAccess(),
            body.user()
        );
        Employee updatedEmployee = employeeService.updateEmployee(toUpdate);

        ctx.json(updatedEmployee);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        employeeService.deleteEmployee(id);

        ctx.json("");
        ctx.status(200);
    }
    
}

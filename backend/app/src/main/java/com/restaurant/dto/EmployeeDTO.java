package com.restaurant.dto;

import com.restaurant.model.User;

public record EmployeeDTO (
    Long id,
    String name,
    String surname,
    String cpf,
    String role,
    boolean hasAccess,
    User user
){}

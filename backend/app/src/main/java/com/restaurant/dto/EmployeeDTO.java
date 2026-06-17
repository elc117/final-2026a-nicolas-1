package com.restaurant.dto;

import com.restaurant.model.User;
import com.restaurant.model.enums.Role;

public record EmployeeDTO (
    Long id,
    String name,
    String surname,
    String cpf,
    Role role,
    User user
){}

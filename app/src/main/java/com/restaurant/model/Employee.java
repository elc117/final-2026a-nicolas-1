package com.restaurant.model;

import com.restaurant.model.enums.Role;
import com.restaurant.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private Long id = null;
    private String name;
    private String surname;
    private String cpf;
    private Role role;
    private User user;

    public Employee(String name, String surname, String cpf, Role role, User user){
        this.name = name;
        this.surname = surname;
        this.cpf = cpf;
        this.role = role;
        this.user = user;
    }

    public Employee(){
        this.name = "Undefined";
        this.name = "Undefined";
        this.cpf = "00000000000";
        this.role = Role.UNDEFINED;
        this.user = null;
    }
}

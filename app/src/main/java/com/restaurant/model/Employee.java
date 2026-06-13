package com.restaurant.model;

import com.restaurant.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private int id;
    private String name;
    private String cpf;
    private Role role;

    public Employee(int id, String name, String cpf, Role role){
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.role = role;
    }

    public Employee(){
        this.id = -1;
        this.name = "Undefined";
        this.cpf = "00000000000";
        this.role = Role.UNDEFINED;
    }
}

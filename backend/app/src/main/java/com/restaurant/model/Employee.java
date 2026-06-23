package com.restaurant.model;

import com.restaurant.dto.EmployeeDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private final Long id;
    private String cpf;
    private String name;
    private String surname;
    private String role;
    private boolean hasAccess;  
    private User user;

    public Employee(Long id, String name, String surname, String cpf, String role, boolean hasAccess, User user){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.cpf = cpf;
        this.role = role;
        this.hasAccess = hasAccess;
        this.user = user;
    }


    // Cria um objeto invalido, esse contrutor nao deve ser utilizado
    public Employee(){
        this.id = null;
        this.name = "Undefined";
        this.cpf = "00000000000";
        this.role = "Undefined";
        this.user = null;
    }
    
    
    public EmployeeDTO toDto() {
        return new EmployeeDTO(
            this.id,
            this.name,
            this.surname,
            this.cpf,
            this.role,
            this.hasAccess,
            this.user
        );
    }

    
}

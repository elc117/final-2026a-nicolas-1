package com.restaurant.model;

import com.restaurant.dto.EmployeeDTO;

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


    // Getters
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getCpf() {
        return this.cpf;
    }

    public String getRole() {
        return this.role;
    }

    public boolean hasAccess() {
        return this.hasAccess;
    }

    public User getUser() {
        return this.user;
    }


    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

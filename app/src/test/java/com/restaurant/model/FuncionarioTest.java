package com.restaurant.model;

import org.junit.jupiter.api.Test;

import com.restaurant.model.enums.Role;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class FuncionarioTest {
    @Test
    @DisplayName("Erro ao criar funcionario com construtor AllArgs")
    void criarFuncionario(){
        Employee func1 = new Employee(1, "Marcelo", "12345678910", Role.COOK);

        assertEquals(1, func1.getId());
        assertEquals("Marcelo", func1.getName());
        assertEquals("12345678910", func1.getCpf());
        assertEquals(Role.COOK, func1.getRole());
    }
}

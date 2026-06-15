package com.restaurant.model;

import com.restaurant.model.enums.MeasurementUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class IngredienteTest {
    @Test
    @DisplayName("Erro ao criar ingrediente com construtor AllArgs")
    void criarIngredienteTeste(){
        Ingredient tomate = new Ingredient("Tomate", MeasurementUnit.KG, 10.0, 2.0);
        
        assertEquals("Tomate", tomate.getName());
        assertEquals(MeasurementUnit.KG, tomate.getMeasurementUnit());
        assertEquals(false, tomate.isEstoqueBaixo());
    }

    @Test
    @DisplayName("Erro ao identificar estoque baixo")
    void alertarEstoqueBaixoTeste(){
        Ingredient tomate = new Ingredient("Tomate", MeasurementUnit.KG, 1.0, 2.0);

        assertEquals(true, tomate.isEstoqueBaixo());
    }
}

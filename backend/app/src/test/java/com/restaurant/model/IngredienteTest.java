package com.restaurant.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.model.enums.MeasurementUnit;

public class IngredienteTest {
    @Test
    @DisplayName("Create ingredient object")
    void createIngredientTest(){
        Ingredient tomate = new Ingredient("Tomate", MeasurementUnit.KG, 10.0, 2.0);
        
        assertEquals("Tomate", tomate.getName());
        assertEquals(MeasurementUnit.KG, tomate.getMeasurementUnit());
        assertEquals(false, tomate.isStockLow());
    }

    @Test
    @DisplayName("Identify low stock")
    void isStockLow(){
        Ingredient tomate = new Ingredient("Tomate", MeasurementUnit.KG, 1.0, 2.0);

        assertEquals(true, tomate.isStockLow());
    }
}

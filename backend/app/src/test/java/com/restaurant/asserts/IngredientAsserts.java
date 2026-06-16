package com.restaurant.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restaurant.model.Ingredient;

public class IngredientAsserts {
    
    public static void assertValidId(Ingredient ingredient) {
        assertNotNull(ingredient.getId(), "Ingredient ID should not be null");
        assertTrue(ingredient.getId() > 0, "Ingredient ID must be greater than 0");
    }
    
    public static void assertEqualIngredients(Ingredient ing1, Ingredient ing2) {
        assertEquals(ing1.getId(), ing2.getId());
        assertEquals(ing1.getName(), ing2.getName());
        assertEquals(ing1.getCurrentAmount(), ing2.getCurrentAmount());
        assertEquals(ing1.getMeasurementUnit(), ing2.getMeasurementUnit());
        assertEquals(ing1.getMinimumStock(), ing2.getMinimumStock());
    }
}

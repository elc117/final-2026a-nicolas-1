package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.Ingredient;
import com.restaurant.model.enums.MeasurementUnit;

public class IngredientFixtures {

    // IDs sao setados como 0, mas serao descartados e substituídos pelo ID definitivo definido pelo DB
    public static Ingredient createTestIngredient() {
        return new Ingredient(Long.valueOf(0), "Tomate", MeasurementUnit.KG, 8.0, 2.0);
    }

    public static List<Ingredient> createTestIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        ingredients.add(new Ingredient(Long.valueOf(0), "Tomate", MeasurementUnit.KG, 8.0, 2.0));
        ingredients.add(new Ingredient(Long.valueOf(0), "Cebola", MeasurementUnit.UNI, 100.0, 50.0));
        ingredients.add(new Ingredient(Long.valueOf(0), "Leite", MeasurementUnit.L, 200.0, 50.0));
        ingredients.add(new Ingredient(Long.valueOf(0), "Frango", MeasurementUnit.KG, 100.0, 80.0));
        
        return ingredients;
    }
}

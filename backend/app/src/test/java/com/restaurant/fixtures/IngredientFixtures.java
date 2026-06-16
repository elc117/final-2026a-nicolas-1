package com.restaurant.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.Ingredient;
import com.restaurant.model.enums.MeasurementUnit;

public class IngredientFixtures {

    public static Ingredient createTestIngredient() {
        return new Ingredient("Tomate", MeasurementUnit.KG, 8.0, 2.0);
    }

    public static List<Ingredient> createTestIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        ingredients.add(new Ingredient("Tomate", MeasurementUnit.KG, 8.0, 2.0));
        ingredients.add(new Ingredient("Cebola", MeasurementUnit.UNI, 100.0, 50.0));
        ingredients.add(new Ingredient("Leite", MeasurementUnit.L, 200.0, 50.0));
        ingredients.add(new Ingredient("Frango", MeasurementUnit.KG, 100.0, 80.0));
        
        return ingredients;
    }
}

package com.restaurant.service;

import com.restaurant.model.Ingredient;
import com.restaurant.repository.IngredientRepository;

import java.util.List;

public class IngredientService {
    
    private final IngredientRepository ingredientRepository;

    public IngredientService() {
        this.ingredientRepository = new IngredientRepository();
    }
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }


    public void registerIngredient(Ingredient ingredient) {
        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name is required");
        }

        if(ingredient.getCurrentAmount() < 0) {
            throw new IllegalArgumentException("Current amount cannot be negative");
        }

        ingredientRepository.add(ingredient);
    }


    public List<Ingredient> listStock() {
        return ingredientRepository.searchAll();
    }


    public Ingredient searchById(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid search ID");
        }

        return ingredientRepository.searchById(id).get();
    }


    public void consumeStock(Long id, Double consumedAmount) {
        Ingredient ingredient = searchById(id);

        if(ingredient.getCurrentAmount() < consumedAmount) {
            throw new IllegalArgumentException("Insufficient amount in stock for ingredient " + ingredient.getName());
        }

        double newAmount = ingredient.getCurrentAmount() - consumedAmount;
        ingredient.setCurrentAmount(newAmount);

        ingredientRepository.update(ingredient);
    }
}

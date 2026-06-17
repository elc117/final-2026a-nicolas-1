package com.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.restaurant.model.Ingredient;
import com.restaurant.repository.IngredientRepository;

public class IngredientService {
    
    private final IngredientRepository ingredientRepository;

    public IngredientService() {
        this.ingredientRepository = new IngredientRepository();
    }

    // Construtor alternativo para testes (Mock repository)
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

        ingredientRepository.save(ingredient.toDto());
    }

    public List<Ingredient> listStock() {
        return ingredientRepository.searchAll();
    }

    public Ingredient searchById(Long id) {
        return ingredientRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("Ingredient with ID " + id + " does not exist"));

    }

    public void updateIngredient(Ingredient ingredient) {
        ingredientRepository.update(ingredient.toDto());
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.delete(id);
    }

    public void consumeStock(Long id, Double consumedAmount) {
        Ingredient ingredient = searchById(id);

        if(ingredient.getCurrentAmount() < consumedAmount) {
            throw new IllegalArgumentException("Insufficient amount in stock for ingredient " + ingredient.getName());
        }

        double newAmount = ingredient.getCurrentAmount() - consumedAmount;
        ingredient.setCurrentAmount(newAmount);

        ingredientRepository.update(ingredient.toDto());
    }
    
}

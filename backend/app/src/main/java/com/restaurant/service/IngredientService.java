package com.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.restaurant.dto.IngredientDTO;
import com.restaurant.model.Ingredient;
import com.restaurant.repository.IngredientRepository;

public class IngredientService {    
    
    private static final IngredientRepository ingredientRepository = new IngredientRepository();

    public Ingredient registerNewIngredient(IngredientDTO dto) {
        checkValidIngredient(dto);
        return ingredientRepository.save(dto);
    }


    public List<Ingredient> listStock() {
        return ingredientRepository.searchAll();
    }


    public Ingredient getIngredientById(Long id) {
        checkValidId(id);
        return ingredientRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("Ingredient with ID " + id + " does not exist"));
    }


    public Ingredient updateIngredient(IngredientDTO dto) {
        checkValidIngredient(dto);
        return ingredientRepository.update(dto);
    }


    public void deleteIngredient(Long id) {
        checkValidId(id);
        ingredientRepository.delete(id);
    }


    
    // Funcoes auxiliares
    private void checkValidIngredient(IngredientDTO dto) {
        if(dto.name() == null || dto.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Ingredient name is required");
        }

        if(dto.measurementUnit() == null) {
            throw new IllegalArgumentException("Measurement unit is required");
        }

        if(dto.currentAmount() < 0) {
            throw new IllegalArgumentException("Current amount cannot be negative");
        }

        if(dto.minimumStock() < 0) {
            throw new IllegalArgumentException("Minimum stock cannot be negative");
        }
    }


    private void checkValidId(Long id) {
        if(id <= 0 || id == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

}

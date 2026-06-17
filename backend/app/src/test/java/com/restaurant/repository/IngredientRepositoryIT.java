package com.restaurant.repository;

import static com.restaurant.asserts.IngredientAsserts.assertEqualIngredients;
import static com.restaurant.asserts.IngredientAsserts.assertValidId;
import static com.restaurant.fixtures.IngredientFixtures.createTestIngredient;
import static com.restaurant.fixtures.IngredientFixtures.createTestIngredients;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.config.BaseIntegrationTest;
import com.restaurant.model.Ingredient;

public class IngredientRepositoryIT extends BaseIntegrationTest{
    private final IngredientRepository repository = new IngredientRepository();

    @Test
    @DisplayName("Add an ingredient to DB")
    void addTest() {
        Ingredient testIngredient = createTestIngredient();

        testIngredient = repository.save(testIngredient.toDto());

        assertValidId(testIngredient);
    }


    @Test
    @DisplayName("Retrieve all ingredients' data from DB")
    void searchIngredientsTest() {
        List<Ingredient> testIngredients = createTestIngredients();

        testIngredients.set(0, repository.save(testIngredients.get(0).toDto()));
        testIngredients.set(1, repository.save(testIngredients.get(1).toDto()));

        List<Ingredient> ingredientsFound = repository.searchAll();

        assertEqualIngredients(testIngredients.get(0), ingredientsFound.get(0));
        assertEqualIngredients(testIngredients.get(1), ingredientsFound.get(1));
    }


    @Test
    @DisplayName("Retrieve an ingredient's data from DB by ID")
    void searchByIdTest() {
        Ingredient testIngredient = createTestIngredient();
        
        testIngredient = repository.save(testIngredient.toDto());

        Optional<Ingredient> foundOptional = repository.searchById(testIngredient.getId());

        assertTrue(foundOptional.isPresent(), "Could not find ingredient by ID in DB");
        Ingredient found = foundOptional.get();
        assertEqualIngredients(testIngredient, found);
    }


    @Test
    @DisplayName("Update an ingredient in DB")
    void updateTest() {
        Ingredient testIngredient = createTestIngredient();
        
        testIngredient = repository.save(testIngredient.toDto());

        testIngredient.setName("Leite Integral");
        testIngredient.setCurrentAmount(18.0);
        
        repository.update(testIngredient.toDto());

        Ingredient updated = repository.searchById(testIngredient.getId()).get();
        
        assertEqualIngredients(testIngredient, updated);
    }


    @Test
    @DisplayName("Delete an ingredient from DB")
    void deleteTest() {
        Ingredient testIngredient = createTestIngredient();
        
        testIngredient = repository.save(testIngredient.toDto());
        repository.delete(testIngredient.getId());

        Optional<Ingredient> deleted = repository.searchById(testIngredient.getId());
        assertFalse(deleted.isPresent(), "Ingredient still exists in DB after deletion");
    }
}

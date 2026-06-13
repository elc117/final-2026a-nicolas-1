package com.restaurant.repository;

import com.restaurant.model.enums.MeasurementUnit;
import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.Ingredient;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class IngredientRepositoryIT {
    private final IngredientRepository repository = new IngredientRepository();

    @BeforeEach
    @AfterEach
    void clearDB() throws Exception {
        try (Connection conn = ConnectionFactory.getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("TRUNCATE TABLE ingredients RESTART IDENTITY");
        }
    }


    @Test
    @DisplayName("Must add an ingredient to DB")
    void addIngredientTest() {
        Ingredient brocolis = new Ingredient("Brocolis", MeasurementUnit.KG, 5.0, 1.5);

        repository.add(brocolis);

        assertNotNull(brocolis.getId(), "Ingredient ID should not be NULL");
        assertTrue(brocolis.getId() > 0, "Generated ID must be greater than 0");
    }


    @Test
    @DisplayName("Must find all ingredients in DB")
    void searchIngredientsTest() {
        Ingredient cebola = new Ingredient("Cebola", MeasurementUnit.UNI, 200.0, 80.0);
        Ingredient tomate = new Ingredient("Tomate", MeasurementUnit.KG, 10.0, 2.0);

        repository.add(cebola);
        repository.add(tomate);
        List<Ingredient> ingredientsFound = repository.searchIngredients();

        assertEquals(ingredientsFound.get(0).getId(), cebola.getId());
        assertEquals(ingredientsFound.get(0).getName(), cebola.getName());
        assertEquals(ingredientsFound.get(1).getId(), tomate.getId());
        assertEquals(ingredientsFound.get(1).getCurrentAmount(), tomate.getCurrentAmount());
    }


    @Test
    @DisplayName("Must find an ingredient by ID in DB")
    void searchIngredientByIdTest() {
        Ingredient alho = new Ingredient("Alho", MeasurementUnit.KG, 5.0, 1.0);
        repository.add(alho);

        Optional<Ingredient> foundOptional = repository.searchIngredientById(alho.getId());

        assertTrue(foundOptional.isPresent(), "Could not find ingredient by ID in DB");
        Ingredient found = foundOptional.get();
        assertEquals("Alho", found.getName());
        assertEquals(MeasurementUnit.KG, found.getMeasurementUnit());
    }


    @Test
    @DisplayName("Must update an ingredient in DB")
    void updateTest() {
        Ingredient leite = new Ingredient("Leite", MeasurementUnit.L, 20.0, 4.0);
        repository.add(leite);

        leite.setName("Leite Integral");
        leite.setCurrentAmount(18.0);
        repository.update(leite);

        Ingredient updated = repository.searchIngredientById(leite.getId()).get();
        
        assertEquals("Leite Integral", updated.getName());
        assertEquals(18.0, updated.getCurrentAmount());
    }


    @Test
    @DisplayName("Must delete an ingredient from DB")
    void deleteTest() {
        Ingredient queijo = new Ingredient("Queijo", MeasurementUnit.KG, 6.0, 2.0);
        repository.add(queijo);

        repository.delete(queijo.getId());

        Optional<Ingredient> deleted = repository.searchIngredientById(queijo.getId());
        assertFalse(deleted.isPresent(), "Ingredient still exists in DB after deletion");
    }
}

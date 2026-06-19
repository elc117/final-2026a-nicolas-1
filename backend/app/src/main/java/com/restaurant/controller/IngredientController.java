package com.restaurant.controller;

import java.util.List;
import java.util.Map;

import com.restaurant.dto.IngredientDTO;
import com.restaurant.model.Ingredient;
import com.restaurant.service.IngredientService;

import io.javalin.http.Context;

public class IngredientController {
    private final IngredientService ingredientService = new IngredientService();
    
    public void register(Context ctx) {
        IngredientDTO dto = ctx.bodyAsClass(IngredientDTO.class);

        Ingredient newIngredient = ingredientService.registerNewIngredient(dto);

        ctx.json(newIngredient);
        ctx.status(201);
    }


    public void list(Context ctx) {
        List<Ingredient> stock = ingredientService.listStock();

        ctx.json(stock);
        ctx.status(200);
    }


    public void getById(Context ctx) {
        IngredientDTO dto = ctx.bodyAsClass(IngredientDTO.class);

        Ingredient ingredient = ingredientService.getIngredientById(dto.id());

        ctx.json(ingredient);
        ctx.status(200);
    }

    
    public void update(Context ctx) {
        IngredientDTO dto = ctx.bodyAsClass(IngredientDTO.class);

        Ingredient updatedIngredient = ingredientService.getIngredientById(dto.id());

        ctx.json(updatedIngredient);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        IngredientDTO dto = ctx.bodyAsClass(IngredientDTO.class);

        ingredientService.deleteIngredient(dto.id());

        ctx.json(Map.of());
        ctx.status(200);
    }
}
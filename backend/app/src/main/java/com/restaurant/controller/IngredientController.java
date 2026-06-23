package com.restaurant.controller;

import java.util.List;

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
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        Ingredient ingredient = ingredientService.getIngredientById(id);

        ctx.json(ingredient);
        ctx.status(200);
    }

    
    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        IngredientDTO body = ctx.bodyAsClass(IngredientDTO.class);

        IngredientDTO toUpdate = new IngredientDTO(
            id,
            body.name(),
            body.measurementUnit(),
            body.currentAmount(),
            body.minimumStock()
        );
        Ingredient updatedIngredient = ingredientService.updateIngredient(toUpdate);
        
        ctx.json(updatedIngredient);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        ingredientService.deleteIngredient(id);

        ctx.json("{}");
        ctx.status(200);
    }
}
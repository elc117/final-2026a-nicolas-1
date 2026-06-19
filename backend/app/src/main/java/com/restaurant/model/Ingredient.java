package com.restaurant.model;

import com.restaurant.dto.IngredientDTO;
import com.restaurant.model.enums.MeasurementUnit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ingredient {
    private final Long id;
    private String name;
    private double currentAmount;
    private MeasurementUnit measurementUnit;
    private double minimumStock;

    public Ingredient(Long id, String name, MeasurementUnit measurementUnit, double currentAmount, double minimumStock) {
        this.id = id;
        this.name = name;
        this.measurementUnit = measurementUnit;
        this.currentAmount = currentAmount;
        this.minimumStock = minimumStock;
    }


    // Cria um bjeto invalido, esse contrutor nao deve ser utilizado
    public Ingredient() {
        this.id = null;
        this.name = null;
        this.measurementUnit = MeasurementUnit.UNDEFINED;
        this.currentAmount = 0.0;
        this.minimumStock = 0.0;
    }


    public boolean isStockLow() {
        return this.currentAmount < this.minimumStock;
    }


    public IngredientDTO toDto() {
        return new IngredientDTO(
            this.id,
            this.name, 
            this.measurementUnit,
            this.currentAmount,
            this.minimumStock
        );
    }
}

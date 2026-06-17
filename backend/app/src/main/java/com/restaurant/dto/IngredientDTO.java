package com.restaurant.dto;

import com.restaurant.model.enums.MeasurementUnit;

public record IngredientDTO (
    Long id, 
    String name, 
    MeasurementUnit measurementUnit, 
    double currentAmount, 
    double minimumStock
){}

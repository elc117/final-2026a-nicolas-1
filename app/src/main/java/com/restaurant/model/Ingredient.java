package com.restaurant.model;

import com.restaurant.model.enums.MeasurementUnit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ingredient {
    private Long id = null;
    private String name;
    private double currentAmount;
    private MeasurementUnit measurementUnit;
    private double minimumStock;

    public Ingredient(String name, MeasurementUnit measurementUnit, double currentAmount, double minimumStock){
        this.name = name;
        this.measurementUnit = measurementUnit;
        this.currentAmount = currentAmount;
        this.minimumStock = minimumStock;
    }

    public Ingredient(){
        this.name = null;
        this.measurementUnit = MeasurementUnit.UNDEFINED;
        this.currentAmount = 0.0;
        this.minimumStock = 0.0;
    }

    public boolean isEstoqueBaixo(){
        return this.currentAmount < this.currentAmount;
    }
}

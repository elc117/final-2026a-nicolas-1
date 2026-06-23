package com.restaurant.dto;

import java.time.LocalDate;

import com.restaurant.model.Ingredient;
import com.restaurant.model.enums.CommChannel;
import com.restaurant.model.enums.OrderStatus;

public record OrderDTO (
    Long id,
    double amount,
    LocalDate date,
    OrderStatus status,
    String contact,
    CommChannel contactChannel,
    Ingredient ingredient
){}

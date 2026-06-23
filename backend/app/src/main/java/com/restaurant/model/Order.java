package com.restaurant.model;

import java.time.LocalDate;

import com.restaurant.dto.OrderDTO;
import com.restaurant.model.enums.CommChannel;
import com.restaurant.model.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private final Long id;
    private double amount;
    private LocalDate date;
    private OrderStatus status;
    private String contact;
    private CommChannel contactChannel;
    private Ingredient ingredient;

    public Order(Long id, Ingredient ingredient, double amount, LocalDate date, OrderStatus status, String contact, CommChannel contactChannel) {
        this.id = id;
        this.ingredient = ingredient;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.contact = contact;
        this.contactChannel = contactChannel;
    }

    public Order(Long id, Ingredient ingredient, double amount, OrderStatus status, String contact, CommChannel contactChannel) {
        this.id = id;
        this.ingredient = ingredient;
        this.amount = amount;
        this.date = LocalDate.now();
        this.status = status;
        this.contact = contact;
        this.contactChannel = contactChannel;
    }

    public Order() {
        this.id = Long.valueOf(0);
        this.ingredient = null;
        this.amount = 0;
        this.date = LocalDate.now();
        this.status = OrderStatus.PENDING;
    }

    public OrderDTO toDto() {
        return new OrderDTO(
            this.id,
            this.amount,
            this.date,
            this.status,
            this.contact,
            this.contactChannel,
            this.ingredient
        );
    }
}

package com.restaurant.fixtures;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.model.Ingredient;
import com.restaurant.model.Order;
import com.restaurant.model.enums.CommChannel;
import com.restaurant.model.enums.OrderStatus;

public class OrderFixtures {

    public static Order createTestOrder(Ingredient ingredient) {
        return new Order(Long.valueOf(0), ingredient, 10.0, LocalDate.now(), OrderStatus.PENDING, "555-0199", CommChannel.WHATSAPP);
    }

    public static List<Order> createTestOrders(Ingredient ingredient1, Ingredient ingredient2) {
        List<Order> orders = new ArrayList<>();

        orders.add(new Order(Long.valueOf(0), ingredient1, 10.0, LocalDate.now(), OrderStatus.PENDING, "555-0199", CommChannel.WHATSAPP));
        orders.add(new Order(Long.valueOf(0), ingredient2, 25.0, LocalDate.now(), OrderStatus.SENT, "order@restaurant.com", CommChannel.EMAIL));

        return orders;
    }
}

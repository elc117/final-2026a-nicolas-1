package com.restaurant.asserts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.restaurant.model.Order;

public class OrderAsserts {
    
    public static void assertValidId(Order order) {
        assertNotNull(order.getId(), "Order ID should not be null");
        assertTrue(order.getId() > 0, "Order ID must be greater than 0");
    }
    
    public static void assertEqualOrders(Order o1, Order o2) {
        assertEquals(o1.getId(), o2.getId());
        assertEquals(o1.getAmount(), o2.getAmount());
        assertEquals(o1.getDate(), o2.getDate());
        assertEquals(o1.getStatus(), o2.getStatus());
        assertEquals(o1.getContact(), o2.getContact());
        assertEquals(o1.getContactChannel(), o2.getContactChannel());
        if (o1.getIngredient() != null && o2.getIngredient() != null) {
            assertEquals(o1.getIngredient().getId(), o2.getIngredient().getId());
            assertEquals(o1.getIngredient().getName(), o2.getIngredient().getName());
        } else {
            assertEquals(o1.getIngredient(), o2.getIngredient());
        }
    }
}

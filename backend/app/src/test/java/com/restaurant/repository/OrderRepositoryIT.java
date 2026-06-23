package com.restaurant.repository;

import static com.restaurant.asserts.OrderAsserts.assertEqualOrders;
import static com.restaurant.asserts.OrderAsserts.assertValidId;
import static com.restaurant.fixtures.OrderFixtures.createTestOrder;
import static com.restaurant.fixtures.OrderFixtures.createTestOrders;
import static com.restaurant.fixtures.IngredientFixtures.createTestIngredient;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.config.BaseIntegrationTest;
import com.restaurant.model.Ingredient;
import com.restaurant.model.Order;
import com.restaurant.model.enums.OrderStatus;

public class OrderRepositoryIT extends BaseIntegrationTest {
    private final OrderRepository orderRepository = new OrderRepository();
    private final IngredientRepository ingredientRepository = new IngredientRepository();

    @Test
    @DisplayName("Add an order to DB")
    void addTest() {
        Ingredient ingredient = ingredientRepository.save(createTestIngredient().toDto());
        Order testOrder = createTestOrder(ingredient);

        testOrder = orderRepository.save(testOrder.toDto());

        assertValidId(testOrder);
    }

    @Test
    @DisplayName("Retrieve all orders from DB")
    void searchOrdersTest() {
        Ingredient ingredient = ingredientRepository.save(createTestIngredient().toDto());
        List<Order> testOrders = createTestOrders(ingredient, ingredient);

        testOrders.set(0, orderRepository.save(testOrders.get(0).toDto()));
        testOrders.set(1, orderRepository.save(testOrders.get(1).toDto()));

        List<Order> ordersFound = orderRepository.searchAll();

        assertTrue(ordersFound.size() >= 2);
        assertEqualOrders(testOrders.get(0), ordersFound.get(0));
        assertEqualOrders(testOrders.get(1), ordersFound.get(1));
    }

    @Test
    @DisplayName("Retrieve an order by ID")
    void searchByIdTest() {
        Ingredient ingredient = ingredientRepository.save(createTestIngredient().toDto());
        Order testOrder = orderRepository.save(createTestOrder(ingredient).toDto());

        Optional<Order> foundOptional = orderRepository.searchById(testOrder.getId());

        assertTrue(foundOptional.isPresent(), "Could not find order by ID");
        assertEqualOrders(testOrder, foundOptional.get());
    }

    @Test
    @DisplayName("Update an order in DB")
    void updateTest() {
        Ingredient ingredient = ingredientRepository.save(createTestIngredient().toDto());
        Order testOrder = orderRepository.save(createTestOrder(ingredient).toDto());

        testOrder.setAmount(50.0);
        testOrder.setStatus(OrderStatus.COMPLETED);

        Order updated = orderRepository.update(testOrder.toDto());

        assertEqualOrders(testOrder, updated);
    }

    @Test
    @DisplayName("Delete an order from DB")
    void deleteTest() {
        Ingredient ingredient = ingredientRepository.save(createTestIngredient().toDto());
        Order testOrder = orderRepository.save(createTestOrder(ingredient).toDto());

        orderRepository.delete(testOrder.getId());

        Optional<Order> deleted = orderRepository.searchById(testOrder.getId());
        assertFalse(deleted.isPresent(), "Order still exists in DB after deletion");
    }
}

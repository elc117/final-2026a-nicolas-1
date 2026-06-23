package com.restaurant.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Ingredient;
import com.restaurant.model.Order;
import com.restaurant.repository.OrderRepository;

public class OrderService {
    private static final IngredientService IngredientService = new IngredientService();
    private static final OrderRepository orderRepository = new OrderRepository();

    public Order registerOrder(OrderDTO dto) {
        checkValidOrder(dto);
        Ingredient ingredient = IngredientService.getIngredientById(dto.ingredient().getId());
        
        dto = new OrderDTO(
            dto.id(),
            dto.amount(),
            dto.date(),
            dto.status(),
            dto.contact(),
            dto.contactChannel(),
            ingredient
        );

        return orderRepository.save(dto);
    }


    public List<Order> listOrders() {
        return orderRepository.searchAll();
    }


    public Order getOrderById(Long id) {
        checkValidId(id);
        return orderRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("Order with ID " + id + " does not exist"));
    }


    public Order updateOrder(OrderDTO dto) {
        checkValidOrder(dto);
        return orderRepository.update(dto);
    }


    public void deleteOrder(Long id) {
        checkValidId(id);
        orderRepository.delete(id);
    }



    // Funções auxiliares
    private static void checkValidId(Long id) {
        if(id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    private void checkValidOrder(OrderDTO dto) {
        if(dto.ingredient() == null) {
            throw new IllegalArgumentException("Ingredient is required");
        }
    } 
}

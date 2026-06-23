package com.restaurant.controller;

import java.util.List;

import com.restaurant.dto.OrderDTO;
import com.restaurant.model.Order;
import com.restaurant.service.OrderService;

import io.javalin.http.Context;

public class OrderController {
    private final OrderService orderService = new OrderService();
    
    public void register(Context ctx) {
        OrderDTO dto = ctx.bodyAsClass(OrderDTO.class);

        Order newOrder = orderService.registerOrder(dto);

        ctx.json(newOrder);
        ctx.status(201);
    }


    public void list(Context ctx) {
        List<Order> orders = orderService.listOrders();

        ctx.json(orders);
        ctx.status(200);
    }


    public void getById(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        Order order = orderService.getOrderById(id);

        ctx.json(order);
        ctx.status(200);
    }


    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        OrderDTO body = ctx.bodyAsClass(OrderDTO.class);

        OrderDTO toUpdate = new OrderDTO(
            id,
            body.amount(),
            body.date(),
            body.status(),
            body.contact(),
            body.contactChannel(),
            body.ingredient()
        );
        Order updatedOrder = orderService.updateOrder(toUpdate);

        ctx.json(updatedOrder);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();

        orderService.deleteOrder(id);

        ctx.json("");
        ctx.status(200);
    }
}

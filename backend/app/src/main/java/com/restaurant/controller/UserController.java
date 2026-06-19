package com.restaurant.controller;

import java.util.List;
import java.util.Map;

import com.restaurant.dto.UserDTO;
import com.restaurant.model.User;
import com.restaurant.service.UserService;

import io.javalin.http.Context;

public class UserController {
    private final UserService userService = new UserService();

    public void register(Context ctx) {
        UserDTO dto = ctx.bodyAsClass(UserDTO.class);

        User newUser = userService.registerUser(dto, dto.password().toCharArray());

        ctx.json(newUser);
        ctx.status(201);
    }

    
    public void list(Context ctx) {
        List<User> users = userService.listUsers();

        ctx.json(users);
        ctx.status(200);
    }


    public void getById(Context ctx) {
        UserDTO dto = ctx.bodyAsClass(UserDTO.class);

        User user = userService.getUserById(dto.id());

        ctx.json(user);
        ctx.status(200);
    }


    public void update(Context ctx) {
        UserDTO dto = ctx.bodyAsClass(UserDTO.class);

        User updatedUser = userService.updateUser(dto);

        ctx.json(updatedUser);
        ctx.status(200);
    }


    public void delete(Context ctx) {
        UserDTO dto = ctx.bodyAsClass(UserDTO.class);

        userService.deleteUser(dto.id());

        ctx.json(Map.of());
        ctx.status(200);
    }
}

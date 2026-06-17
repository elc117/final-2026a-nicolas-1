package com.restaurant.dto;

import com.restaurant.model.enums.AccessProfile;

public record UserDTO (
    Long id,
    String login,
    String password,
    AccessProfile accessProfile
){}

package com.restaurant.model;

import com.restaurant.model.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id = null;
    private String login;
    private String password;
    private Role accessProfile;

    public User(String login, String password, Role accessProfile) {
        this.login = login;
        this.password = password;
        this.accessProfile = accessProfile;
    }

    public User() {
        this.login = null;
        this.password = null;
        this.accessProfile = null;
    }
}

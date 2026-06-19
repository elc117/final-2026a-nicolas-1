package com.restaurant.model;

import com.restaurant.dto.UserDTO;
import com.restaurant.model.enums.AccessProfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private final Long id;
    private String login;
    private String password;
    private AccessProfile accessProfile;

    public User(Long id, String login, String password, AccessProfile accessProfile) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.accessProfile = accessProfile;
    }


    // Cria um objeto invalido, esse construtor nao deve ser utilizado
    public User() {
        this.id = null;
        this.login = null;
        this.password = null;
        this.accessProfile = AccessProfile.UNDEFINED;
    }

    
    public UserDTO toDto() {
        return new UserDTO(
            this.id,
            this.login,
            this.password,
            this.accessProfile
        );
    }
}

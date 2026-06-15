package com.restaurant.service;

import com.restaurant.repository.UserRepository;
import com.restaurant.model.User;
import com.restaurant.config.CryptoUtils;

import java.util.Arrays;
import java.util.Optional;

public class UserService {
    
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    // Construtor alternativo para testes (Mock repository)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void registerUser(User user, char[] purePassword) {
        if(user.getId() != null) {
            throw new IllegalArgumentException("ID is defined by database, should be NULL");
        }

        Optional<User> existing = userRepository.searchByLogin(user.getLogin());
        if(existing.isPresent()) {
            throw new IllegalArgumentException("User with input login already exists");
        }

        String safeHash = CryptoUtils.generateHash(purePassword);
        user.setPassword(safeHash);
        Arrays.fill(purePassword, '0');
        userRepository.add(user);
    }


    public boolean auth(String login, char[] inputPassword) {
        User user = userRepository.searchByLogin(login).get();

        boolean validPassword = CryptoUtils.checkPassword(inputPassword, user.getPassword());
        Arrays.fill(inputPassword, '0');
        return validPassword;
    }
}

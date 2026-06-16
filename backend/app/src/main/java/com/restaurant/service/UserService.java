package com.restaurant.service;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.restaurant.config.CryptoUtils;
import com.restaurant.model.User;
import com.restaurant.repository.UserRepository;

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

        userRepository.searchByLogin(user.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("User with input login already exists"));

        String safeHash = CryptoUtils.generateHash(purePassword);

        user.setPassword(safeHash);
        Arrays.fill(purePassword, '0');

        userRepository.save(user);
    }


    public User searchById(Long id) {
        checkId(id);
        
        return userRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " does not exist"));
    } 


    public boolean auth(String login, char[] inputPassword) {
        User user = userRepository.searchByLogin(login).get();

        boolean validPassword = CryptoUtils.checkPassword(inputPassword, user.getPassword());
        Arrays.fill(inputPassword, '0');

        return validPassword;
    }


    private void checkId(Long id) {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("Invalid ID");
    }
}

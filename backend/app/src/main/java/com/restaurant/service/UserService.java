package com.restaurant.service;

import java.util.Arrays;
import java.util.NoSuchElementException;

import com.restaurant.model.User;
import com.restaurant.repository.UserRepository;
import com.restaurant.utils.CryptoUtils;

public class UserService {
    
    private static final UserRepository userRepository = new UserRepository();

    public static void registerUser(User user, char[] purePassword) {
        validateNewUser(user);
        String safeHash = CryptoUtils.generateHash(purePassword);

        user.setPassword(safeHash);
        Arrays.fill(purePassword, '0');

        userRepository.save(user.toDto());
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

    public static void validateNewUser(User user) {
        if(user.getId() != null) {
            throw new IllegalArgumentException("ID is defined by database, should be null");
        }

        userRepository.searchByLogin(user.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("User with input login already exists"));

        // String safeHash = CryptoUtils.generateHash(user.getPassword().toCharArray());
        //userRepository.searchByPassword(safeHash)
        //    .orElseThrow(() -> new IllegalArgumentException("User with this password already exists"));
    }
}

package com.restaurant.service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.restaurant.dto.UserDTO;
import com.restaurant.model.User;
import com.restaurant.repository.UserRepository;
import com.restaurant.utils.CryptoUtils;

public class UserService {
    
    private static final UserRepository userRepository = new UserRepository();

    public User registerUser(UserDTO dto, char[] purePassword) {
        validateNewUser(dto);
        String safeHash = CryptoUtils.generateHash(purePassword);

        dto = new UserDTO(
            dto.id(),
            dto.login(),
            safeHash,
            dto.accessProfile()
        );
        
        Arrays.fill(purePassword, '0');

        return userRepository.save(dto);
    }


    public List<User> listUsers() {
        return userRepository.searchAll();
    }


    public User getUserById(Long id) {
        checkId(id);
        return userRepository.searchById(id)
            .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " does not exist"));
    } 


    public User getUserByPassword(Long hash) {
        return userRepository.searchById(hash)
            .orElseThrow(() -> new NoSuchElementException("User with this password does not exist"));
    }


    public User updateUser(UserDTO dto) {
        checkValidUser(dto);
        return userRepository.update(dto);
    }


    public void deleteUser(Long id) {
        userRepository.delete(id);
    }



    public boolean auth(String login, char[] inputPassword) {
        User user = userRepository.searchByLogin(login).get();

        boolean validPassword = CryptoUtils.checkPassword(inputPassword, user.getPassword());
        Arrays.fill(inputPassword, '0');

        return validPassword;
    }


    
    // Funcoes auxiliares
    private void checkId(Long id) {
        if(id == null || id <= 0)
            throw new IllegalArgumentException("Invalid ID");
    }


    private void checkValidUser(UserDTO dto) {
        if(dto.login() == null || dto.login().trim().isEmpty()) {
            throw new IllegalArgumentException("User login is required");
        }

        if(dto.password() == null || dto.password().trim().isEmpty()) {
            throw new IllegalArgumentException("User password is required");
        }
    }


    public void validateNewUser(UserDTO dto) {
        checkValidUser(dto);

        userRepository.searchByLogin(dto.login())
            .orElseThrow(() -> new IllegalArgumentException("User with input login already exists"));

        String safeHash = CryptoUtils.generateHash(dto.password().toCharArray());
        userRepository.searchByPassword(safeHash)
            .orElseThrow(() -> new IllegalArgumentException("User with this password already exists"));
    }
}

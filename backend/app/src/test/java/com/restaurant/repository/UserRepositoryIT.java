package com.restaurant.repository;

import static com.restaurant.asserts.UserAsserts.assertEqualUsers;
import static com.restaurant.asserts.UserAsserts.assertValidId;
import static com.restaurant.fixtures.UserFixtures.createTestUser;
import static com.restaurant.fixtures.UserFixtures.createTestUsers;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.restaurant.config.BaseIntegrationTest;
import com.restaurant.model.User;
import com.restaurant.model.enums.AccessProfile;

public class UserRepositoryIT extends BaseIntegrationTest{
    private final UserRepository repository = new UserRepository();

    @Test
    @DisplayName("Add an user to DB")
    void addTest() {
        User testUser = createTestUser();

        testUser = repository.save(testUser.toDto());

        assertValidId(testUser);
    }


    @Test
    @DisplayName("Retrieve all users' data from DB")
    void searchAllTest() {
        List<User> testUsers = createTestUsers();

        testUsers.set(0, repository.save(testUsers.get(0).toDto()));
        testUsers.set(1, repository.save(testUsers.get(1).toDto()));

        List<User> usersFound = repository.searchAll();

        assertEqualUsers(testUsers.get(0), usersFound.get(0));
        assertEqualUsers(testUsers.get(1), usersFound.get(1));
    }


    @Test
    @DisplayName("Retrieve an user's data from DB by ID")
    void searchByIdTest() {
        User testUser = createTestUser();

        testUser = repository.save(testUser.toDto());

        Optional<User> foundOptional = repository.searchById(testUser.getId());
        
        assertTrue(foundOptional.isPresent(), "Could not find user by ID in DB");
        User found = foundOptional.get();
        assertEqualUsers(testUser, found);
    }


    @Test
    @DisplayName("Retrieve an user's data from DB by login")
    void searchByLoginTest() {
        User testUser = createTestUser();

        testUser = repository.save(testUser.toDto());

        Optional<User> foundOptional = repository.searchByLogin(testUser.getLogin());

        assertTrue(foundOptional.isPresent(), "Could not find user by login in DB");
        User found = foundOptional.get();
        assertEqualUsers(testUser, found);
    }

    
    @Test
    @DisplayName("Update an user's data in DB")
    void updateTest() {
        User testUser = createTestUser();

        testUser = repository.save(testUser.toDto());

        testUser.setLogin("nicolas123");
        testUser.setAccessProfile(AccessProfile.CHEF);

        repository.update(testUser.toDto());
        User updated = repository.searchById(testUser.getId()).get();

        assertEqualUsers(testUser, updated);
    }


    @Test
    @DisplayName("Delete an user's data in DB")
    void deleteTest() {
        User testUser = createTestUser();

        testUser = repository.save(testUser.toDto());
        repository.delete(testUser.getId());

        Optional<User> deleted = repository.searchById(testUser.getId());
        assertFalse(deleted.isPresent(), "User still exists in DB after deletion");
    }
}

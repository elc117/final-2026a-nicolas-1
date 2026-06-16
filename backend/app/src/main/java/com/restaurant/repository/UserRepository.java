package com.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.User;
import com.restaurant.model.enums.AccessProfile;

public class UserRepository {
    
    public void save(User user) {
        String sql = "INSERT INTO users (login, password, access_profile) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getAccessProfile().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not add user to DB", e);
        }
    }


    public List<User> searchAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                User someUser = mapResultSetToUser(rs);
                users.add(someUser);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve users from DB", e);
        }
        return users;
    }


    public Optional<User> searchById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve user data from DB", e);
        }
        return Optional.empty();
    }


    public Optional<User> searchByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve user data from DB", e);
        }
        return Optional.empty();
    }

    
    public void update(User user) {
        String sql = "UPDATE users SET login = ?, password = ?, access_profile = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getAccessProfile().name());
            stmt.setLong(4, user.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update user in DB", e);
        }
    }


    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete user from DB", e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setAccessProfile(AccessProfile.valueOf(rs.getString("access_profile")));

        return user;
    }
    
}

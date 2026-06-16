package com.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.Employee;
import com.restaurant.model.User;
import com.restaurant.model.enums.AccessProfile;
import com.restaurant.model.enums.Role;

public class EmployeeRepository {
    
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (cpf, name, surname, role, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employee.getCpf());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getSurname());
            stmt.setString(4, employee.getRole().name());

            if (employee.getUser() != null && employee.getUser().getId() != null) {
                stmt.setLong(5, employee.getUser().getId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    employee.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not add employee to DB");
        }
    }


    public List<Employee> searchAll() {
        String sql = "SELECT e.id AS employee_id, e.cpf, e.name, e.surname, e.role, " +
                     "u.id AS user_id, u.login, u.password, u.access_profile FROM employees e " +
                     "LEFT JOIN users u ON e.user_id = u.id"; 

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            List<Employee> employees = new ArrayList<>();
            while(rs.next()) {
                Employee someEmployee = mapResultSetToEmployee(rs);
                employees.add(someEmployee);
            }
            return employees;

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve employees data from DB");
        }
    }


    public Optional<Employee> searchById(Long id) {
        String sql = "SELECT e.id AS employee_id, e.cpf, e.name, e.surname, e.role, " +
                     "u.id AS user_id, u.login, u.password, u.access_profile FROM employees e " +
                     "LEFT JOIN users u ON e.user_id = u.id WHERE e.id = ?"; 

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(mapResultSetToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve employee data from DB");
        }
        return Optional.empty();
    }


    public void update(Employee employee) {
        String sql = "UPDATE employees SET cpf = ?, name = ?, surname = ?, role = ?, user_id = ? WHERE id = ?";

        Long userId = null;
        if (employee.getUser() != null) {
            userId = employee.getUser().getId();

            if (userId == null) {
                Optional<Employee> currentEmployee = searchById(employee.getId());

                if (currentEmployee.isPresent() && currentEmployee.get().getUser() != null) {
                    userId = currentEmployee.get().getUser().getId();
                    employee.getUser().setId(userId);
                }
            }

            if (userId != null) {
                new UserRepository().update(employee.getUser());
            }
        }

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getCpf());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getSurname());
            stmt.setString(4, employee.getRole().name());
            if (userId != null) {
                stmt.setLong(5, userId);
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            stmt.setLong(6, employee.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update employee data in DB");
        }
    }


    public void delete(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete employee data from DB");
        }
    }


    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        
        employee.setId(rs.getLong("employee_id"));
        employee.setCpf(rs.getString("cpf"));
        employee.setName(rs.getString("name"));
        employee.setSurname(rs.getString("surname"));
        employee.setRole(Role.valueOf(rs.getString("role")));
        
        long userId = rs.getLong("user_id");

        if (!rs.wasNull()) {
            User user = new User();
            user.setId(userId);
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setAccessProfile(AccessProfile.valueOf(rs.getString("access_profile")));

            employee.setUser(user);
        } else {
            employee.setUser(null);
        }
        return employee;
    }
}

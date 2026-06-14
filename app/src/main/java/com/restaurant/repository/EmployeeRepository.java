package com.restaurant.repository;

import com.restaurant.model.Employee;
import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.enums.Employment;
import com.restaurant.model.enums.Role;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class EmployeeRepository {
    
    public void add(Employee employee) {
        String sql = "INSERT INTO employees (cpf, name, surname, role, user) VALUES (?, ?, ?, ?, ?))";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getCpf());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getSurname());
            stmt.setString(4, employee.getRole().name());

            if (employee.getUser() != null && employee.getUser().getId() != null) {
                stmt.setLong(4, employee.getUser().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
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
        String sql = "UPDATE employees SET cpf = ?, name = ?, surname = ?, role = ?, user_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getCpf());
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getSurname());
            stmt.setString(4, employee.getRole().name());
            stmt.setLong(5, employee.getUser().getId());

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


    private Employee mapResultSetTEmployee(ResultSet rs) {
        Employee employee = new Employee();
        
        employee.setCpf(rs.getString("cpf"));
        employee.setName(rs.getString("name"));
        employee.setSurname(rs.getString("surname"));
        employee.setRole(Role.valueOf(rs.getString("role")));
        employee.setUser();

    }
}

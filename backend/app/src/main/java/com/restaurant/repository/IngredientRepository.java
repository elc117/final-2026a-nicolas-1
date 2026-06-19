package com.restaurant.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.restaurant.dto.IngredientDTO;
import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.Ingredient;
import com.restaurant.model.enums.MeasurementUnit;

public class IngredientRepository {

    public Ingredient save(IngredientDTO ingredientDTO) {
        String sql = "INSERT INTO ingredients (name, measurement_unit, current_amount, minimum_stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, ingredientDTO.name());
            stmt.setString(2, ingredientDTO.measurementUnit().name());
            stmt.setDouble(3, ingredientDTO.currentAmount());
            stmt.setDouble(4, ingredientDTO.minimumStock());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    return new Ingredient(
                        rs.getLong(1),
                        ingredientDTO.name(),
                        ingredientDTO.measurementUnit(),
                        ingredientDTO.currentAmount(),
                        ingredientDTO.minimumStock()
                    );
                } else {
                    throw new SQLException("Could not get DB generated ID");
                }
            }
    
        } catch (SQLException e) {
            throw new RuntimeException("Could not add ingredient to DB", e);
        }
    }


    public List<Ingredient> searchAll() {
        String sql = "SELECT * FROM ingredients";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

            while(rs.next()) {
                Ingredient ing = mapResultSetToIngredient(rs);
                ingredients.add(ing);
            }
                
        } catch (SQLException e){
            throw new RuntimeException("Could not list all ingredients in DB", e);
        }
        return ingredients;
    }


    public Optional<Ingredient> searchById(Long id) {
        String sql = "SELECT * FROM ingredients WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToIngredient(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not retrieve ingredient by ID in DB", e);
        }
        return Optional.empty();
    }

    
    public Ingredient update(IngredientDTO ingredientDTO) {
        String sql = "UPDATE ingredients SET name = ?, measurement_unit = ?, current_amount = ?, minimum_stock = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingredientDTO.name());
            stmt.setString(2, ingredientDTO.measurementUnit().name());
            stmt.setDouble(3, ingredientDTO.currentAmount());
            stmt.setDouble(4, ingredientDTO.minimumStock());
            stmt.setLong(5, ingredientDTO.id());

            stmt.executeUpdate();

            try(ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()) {
                    return mapResultSetToIngredient(rs);
                } else {
                    throw new SQLException("Could not retrieve updated ingredient data");
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Could not update ingredient in DB");
        }
    }


    public void delete(Long id) {
        String sql = "DELETE FROM ingredients WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete ingredient from DB");
        }
    }


    
    // Mapeia ResultSet para um objeto da classe Ingredient - utilizado nas buscas
    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException{

        return new Ingredient(
            rs.getLong("id"), 
            rs.getString("name"),
            MeasurementUnit.valueOf(rs.getString("measurement_unit")),
            rs.getDouble("current_amount"),
            rs.getLong("minimum_stock")
        );
        
    }

}

package com.restaurant.repository;

import com.restaurant.config.ConnectionFactory;
import com.restaurant.model.Ingredient;
import com.restaurant.model.enums.MeasurementUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientRepository {

    public void addIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredientes (nome, quantidade, unidade_medida, estoque_minimo) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, ingredient.getName());
            stmt.setDouble(2, ingredient.getCurrentAmount());
            stmt.setString(3, ingredient.getMeasurementUnit().name());
            stmt.setDouble(4, ingredient.getMinimumStock());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if(rs.next()){
                    ingredient.setId((rs.getLong(1)));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error: could not add ingredient in DB", e);
        }
    }


    public List<Ingredient> searchAll() {
        String sql = "SELECT * FROM ingredientes";
        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();) {

                while(rs.next()) {
                    Ingredient ing = mapResultSetToIngredient(rs);
                    ingredients.add(ing);
                }
        } catch (SQLException e){
            throw new RuntimeException("Error in listing ingredients in DB", e);
        }
        return ingredients;
    }


    public Optional<Ingredient> searchById(Long id) {
        String sql = "SELECT * FROM ingredientes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, id);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToIngredient(rs));
                    }
                }

        } catch (SQLException e) {
            throw new RuntimeException("Error in finding ingredient by ID in DB", e);
        }
        return Optional.empty();
    }

    
    public void update(Ingredient ingredient) {
        String sql = "UPDATE ingredients SET name = ?, current_amount = ?, measurement_unit = ?, minimum_stock = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, ingredient.getName());
                stmt.setDouble(2, ingredient.getCurrentAmount());
                stmt.setString(3, ingredient.getMeasurementUnit().name());
                stmt.setDouble(4, ingredient.getMinimumStock());
                stmt.setLong(5, ingredient.getId());

                stmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Error in updating ingredient in DB");
        }
    }


    public void delete(Long id) {
        String sql = "DELETE FROM ingredients WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setLong(1, id);
                stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error in deleting ingredient from DB");
        }
    }

    // Mapeia ResultSet para um objeto da classe Ingredient - utilizado nas buscas
    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException{
        Ingredient ing = new Ingredient();
        ing.setId(rs.getLong("id"));
        ing.setName(rs.getString("name"));
        ing.setCurrentAmount(rs.getDouble("current_amount"));
        ing.setMeasurementUnit(MeasurementUnit.valueOf(rs.getString("measurement_unit")));
        ing.setMinimumStock(rs.getLong("minimum_stock"));
        return ing;
    }

}

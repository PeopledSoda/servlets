package ru.itis.repositories;

import ru.itis.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "1435679399";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test_db";

    public UserRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("PostgreSQL driver is not available", e);
        }
    }

    public int getUsersToAdd() throws SQLException {
        String sql = "SELECT users_to_add FROM user_generation_settings WHERE id = 1";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException("The number of users to add is not configured");
            }
            int usersToAdd = resultSet.getInt("users_to_add");
            if (usersToAdd < 1) {
                throw new SQLException("The number of users to add must be positive");
            }
            return usersToAdd;
        }
    }

    public void saveUsers(List<User> users) throws SQLException {
        String sql = "INSERT INTO users (name, surname, age, email, city, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                for (User user : users) {
                    statement.setString(1, user.getName());
                    statement.setString(2, user.getSurname());
                    statement.setInt(3, user.getAge());
                    statement.setString(4, user.getEmail());
                    statement.setString(5, user.getCity());
                    statement.setString(6, user.getPhone());
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        return find("SELECT id, name, surname, age, email, city, phone FROM users");
    }

    public List<User> findByEmail(String email) throws SQLException {
        return find("SELECT id, name, surname, age, email, city, phone FROM users WHERE email = ?",
                email);
    }

    public List<User> findByCity(String city) throws SQLException {
        return find("SELECT id, name, surname, age, email, city, phone FROM users WHERE city = ?",
                city);
    }

    public List<User> findByPhone(String phone) throws SQLException {
        return find("SELECT id, name, surname, age, email, city, phone FROM users WHERE phone = ?",
                phone);
    }

    private List<User> find(String sql, String... parameters) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setString(i + 1, parameters[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(User.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .surname(resultSet.getString("surname"))
                            .age(resultSet.getInt("age"))
                            .email(resultSet.getString("email"))
                            .city(resultSet.getString("city"))
                            .phone(resultSet.getString("phone"))
                            .build());
                }
            }
        }
        return users;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}

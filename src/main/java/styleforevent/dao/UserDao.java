package styleforevent.dao;

import styleforevent.model.User;
import styleforevent.util.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class UserDao {
    private static final Logger logger = LogManager.getLogger(UserDao.class);

    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        logger.debug("SQL: {}", sql);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHash());

            boolean result = stmt.executeUpdate() > 0;
            logger.debug("Результат создания пользователя: success={}, username={}", result, user.getUsername());
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при создании пользователя: username={}", user.getUsername(), e);
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        logger.debug("SQL: {}, username={}", sql, username);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                logger.trace("Пользователь найден: username={}", username);
                return Optional.of(user);
            }

            logger.trace("Пользователь не найден: username={}", username);
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("Ошибка при поиске пользователя: username={}", username, e);
            return Optional.empty();
        }
    }
}
package styleforevent.dao;

import styleforevent.util.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoritesDao {
    private static final Logger logger = LogManager.getLogger(FavoritesDao.class);

    public boolean addToFavorites(Long userId, Long outfitId) {
        String sql = "INSERT INTO user_favorite_outfits (user_id, outfit_id) VALUES (?, ?)";
        logger.debug("SQL: {}, userId={}, outfitId={}", sql, userId, outfitId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, outfitId);

            int rowsAffected = stmt.executeUpdate();
            boolean result = rowsAffected > 0;

            logger.debug("Результат добавления в избранное: success={}, userId={}, outfitId={}",
                    result, userId, outfitId);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при добавлении в избранное: userId={}, outfitId={}", userId, outfitId, e);
            return false;
        }
    }

    public boolean removeFromFavorites(Long userId, Long outfitId) {
        String sql = "DELETE FROM user_favorite_outfits WHERE user_id = ? AND outfit_id = ?";
        logger.debug("SQL: {}, userId={}, outfitId={}", sql, userId, outfitId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, outfitId);

            int rowsAffected = stmt.executeUpdate();
            boolean result = rowsAffected > 0;

            logger.debug("Результат удаления из избранного: success={}, userId={}, outfitId={}",
                    result, userId, outfitId);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении из избранного: userId={}, outfitId={}", userId, outfitId, e);
            return false;
        }
    }

    public List<Long> getUserFavoriteIds(Long userId) {
        String sql = "SELECT outfit_id FROM user_favorite_outfits WHERE user_id = ?";
        logger.debug("SQL: {}, userId={}", sql, userId);

        List<Long> outfitIds = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                outfitIds.add(rs.getLong("outfit_id"));
                count++;
            }

            logger.trace("Найдено избранных образов: userId={}, count={}", userId, count);
        } catch (SQLException e) {
            logger.error("Ошибка при получении избранного: userId={}", userId, e);
        }
        return outfitIds;
    }

    public boolean isFavorite(Long userId, Long outfitId) {
        String sql = "SELECT 1 FROM user_favorite_outfits WHERE user_id = ? AND outfit_id = ?";
        logger.trace("SQL: {}, userId={}, outfitId={}", sql, userId, outfitId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, outfitId);

            ResultSet rs = stmt.executeQuery();
            boolean result = rs.next();

            logger.trace("Проверка избранного: isFavorite={}, userId={}, outfitId={}", result, userId, outfitId);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при проверке избранного: userId={}, outfitId={}", userId, outfitId, e);
            return false;
        }
    }
}
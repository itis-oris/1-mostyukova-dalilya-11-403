package styleforevent.dao;

import styleforevent.model.Outfit;
import styleforevent.util.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OutfitDao {
    private static final Logger logger = LogManager.getLogger(OutfitDao.class);

    public boolean create(Outfit outfit) {
        String sql = "INSERT INTO outfits (name, description, image_url, user_id, event_id, gender) VALUES (?, ?, ?, ?, ?, ?)";
        logger.debug("SQL: {}, name={}", sql, outfit.getName());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, outfit.getName());
            stmt.setString(2, outfit.getDescription());
            stmt.setString(3, outfit.getImageUrl());
            stmt.setLong(4, outfit.getUserId());
            stmt.setLong(5, outfit.getEventId());
            stmt.setString(6, outfit.getGender());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        outfit.setId(generatedKeys.getLong(1));
                        logger.debug("Образ создан: outfitId={}, name={}", outfit.getId(), outfit.getName());
                        return true;
                    }
                }
            }

            logger.warn("Не удалось создать образ: name={}", outfit.getName());
            return false;
        } catch (SQLException e) {
            logger.error("Ошибка при создании образа: name={}", outfit.getName(), e);
            return false;
        }
    }

    public List<Outfit> findByUserId(Long userId) {
        String sql = "SELECT * FROM outfits WHERE user_id = ? ORDER BY created_at DESC";
        logger.debug("SQL: {}, userId={}", sql, userId);

        List<Outfit> outfits = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                outfits.add(mapResultSetToOutfit(rs));
            }

            logger.trace("Найдено образов для пользователя: userId={}, count={}", userId, outfits.size());
        } catch (SQLException e) {
            logger.error("Ошибка при поиске образов пользователя: userId={}", userId, e);
        }
        return outfits;
    }

    public List<Outfit> findByEventAndGender(Long eventId, String gender) {
        String sql = "SELECT * FROM outfits WHERE event_id = ? AND gender = ? ORDER BY created_at DESC";
        logger.debug("SQL: {}, eventId={}, gender={}", sql, eventId, gender);

        List<Outfit> outfits = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, eventId);
            stmt.setString(2, gender);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                outfits.add(mapResultSetToOutfit(rs));
            }

            logger.trace("Найдено образов по фильтру: eventId={}, gender={}, count={}",
                    eventId, gender, outfits.size());
        } catch (SQLException e) {
            logger.error("Ошибка при поиске образов по фильтру: eventId={}, gender={}", eventId, gender, e);
        }
        return outfits;
    }

    public Outfit findById(Long id) {
        String sql = "SELECT * FROM outfits WHERE id = ?";
        logger.debug("SQL: {}, outfitId={}", sql, id);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Outfit outfit = mapResultSetToOutfit(rs);
                logger.trace("Образ найден: outfitId={}, name={}", id, outfit.getName());
                return outfit;
            }

            logger.trace("Образ не найден: outfitId={}", id);
            return null;
        } catch (SQLException e) {
            logger.error("Ошибка при поиске образа: outfitId={}", id, e);
            return null;
        }
    }

    public boolean update(Outfit outfit) {
        String sql = "UPDATE outfits SET name = ?, description = ?, image_url = ?, event_id = ?, gender = ? WHERE id = ?";
        logger.debug("SQL: {}, outfitId={}", sql, outfit.getId());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, outfit.getName());
            stmt.setString(2, outfit.getDescription());
            stmt.setString(3, outfit.getImageUrl());
            stmt.setLong(4, outfit.getEventId());
            stmt.setString(5, outfit.getGender());
            stmt.setLong(6, outfit.getId());

            boolean result = stmt.executeUpdate() > 0;
            logger.debug("Результат обновления образа: success={}, outfitId={}", result, outfit.getId());
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении образа: outfitId={}", outfit.getId(), e);
            return false;
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM outfits WHERE id = ?";
        logger.debug("SQL: {}, outfitId={}", sql, id);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            boolean result = stmt.executeUpdate() > 0;
            logger.debug("Результат удаления образа: success={}, outfitId={}", result, id);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении образа: outfitId={}", id, e);
            return false;
        }
    }

    private Outfit mapResultSetToOutfit(ResultSet rs) throws SQLException {
        Outfit outfit = new Outfit();
        outfit.setId(rs.getLong("id"));
        outfit.setName(rs.getString("name"));
        outfit.setDescription(rs.getString("description"));
        outfit.setImageUrl(rs.getString("image_url"));
        outfit.setUserId(rs.getLong("user_id"));
        outfit.setEventId(rs.getLong("event_id"));
        outfit.setGender(rs.getString("gender"));
        outfit.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return outfit;
    }
}
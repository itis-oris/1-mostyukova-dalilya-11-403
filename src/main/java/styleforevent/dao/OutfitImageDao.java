package styleforevent.dao;

import styleforevent.model.OutfitImage;
import styleforevent.util.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OutfitImageDao {
    private static final Logger logger = LogManager.getLogger(OutfitImageDao.class);

    public boolean create(OutfitImage image) {
        String sql = "INSERT INTO outfit_images (outfit_id, image_url) VALUES (?, ?)";
        logger.debug("SQL: {}, outfitId={}", sql, image.getOutfitId());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, image.getOutfitId());
            stmt.setString(2, image.getImageUrl());

            boolean result = stmt.executeUpdate() > 0;
            logger.trace("Результат создания изображения: success={}, outfitId={}", result, image.getOutfitId());
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при создании изображения: outfitId={}", image.getOutfitId(), e);
            return false;
        }
    }

    public List<OutfitImage> findByOutfitId(Long outfitId) {
        String sql = "SELECT * FROM outfit_images WHERE outfit_id = ? ORDER BY created_at";
        logger.debug("SQL: {}, outfitId={}", sql, outfitId);

        List<OutfitImage> images = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, outfitId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                images.add(mapResultSetToImage(rs));
            }

            logger.trace("Найдено изображений для образа: outfitId={}, count={}", outfitId, images.size());
        } catch (SQLException e) {
            logger.error("Ошибка при поиске изображений: outfitId={}", outfitId, e);
        }
        return images;
    }

    public boolean delete(Long imageId) {
        String sql = "DELETE FROM outfit_images WHERE id = ?";
        logger.debug("SQL: {}, imageId={}", sql, imageId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, imageId);
            boolean result = stmt.executeUpdate() > 0;
            logger.debug("Результат удаления изображения: success={}, imageId={}", result, imageId);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении изображения: imageId={}", imageId, e);
            return false;
        }
    }

    public boolean deleteByOutfitId(Long outfitId) {
        String sql = "DELETE FROM outfit_images WHERE outfit_id = ?";
        logger.debug("SQL: {}, outfitId={}", sql, outfitId);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, outfitId);
            boolean result = stmt.executeUpdate() > 0;
            logger.debug("Результат удаления изображений образа: success={}, outfitId={}", result, outfitId);
            return result;
        } catch (SQLException e) {
            logger.error("Ошибка при удалении изображений образа: outfitId={}", outfitId, e);
            return false;
        }
    }

    private OutfitImage mapResultSetToImage(ResultSet rs) throws SQLException {
        OutfitImage image = new OutfitImage();
        image.setId(rs.getLong("id"));
        image.setOutfitId(rs.getLong("outfit_id"));
        image.setImageUrl(rs.getString("image_url"));
        image.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return image;
    }
}
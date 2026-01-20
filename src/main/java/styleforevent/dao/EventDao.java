package styleforevent.dao;

import styleforevent.model.Event;
import styleforevent.util.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    private static final Logger logger = LogManager.getLogger(EventDao.class);

    public List<Event> findAll() {
        String sql = "SELECT * FROM events";
        logger.debug("SQL: {}", sql);

        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getLong("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                events.add(event);
            }

            logger.debug("Загружено мероприятий из БД: count={}", events.size());
        } catch (SQLException e) {
            logger.error("Ошибка при загрузке мероприятий", e);
        }
        return events;
    }
}
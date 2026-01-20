package styleforevent.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    private static final String URL = "jdbc:postgresql://localhost:5432/event_outfit_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            logger.debug("PostgreSQL драйвер успешно загружен");
        } catch (ClassNotFoundException e) {
            logger.fatal("PostgreSQL драйвер не найден", e);
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        logger.trace("Создание подключения к БД");
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        logger.trace("Подключение к БД установлено");
        return connection;
    }
}
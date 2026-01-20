package styleforevent.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import styleforevent.util.DatabaseConnection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("=========================================");
        logger.info("Запуск приложения Event Outfit");
        logger.info("=========================================");

        testDatabaseConnection();
        logger.info("Приложение успешно инициализировано");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("=========================================");
        logger.info("Остановка приложения Event Outfit");
        logger.info("=========================================");
        logger.info("Приложение успешно остановлено");
    }

    private void testDatabaseConnection() {
        logger.info("Тестирование подключения к БД...");
        try (Connection connection = DatabaseConnection.getConnection()) {
            logger.info("Подключение к БД успешно установлено");
        } catch (SQLException e) {
            logger.error("Ошибка подключения к БД", e);
        }
    }
}
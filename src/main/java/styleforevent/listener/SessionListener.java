package styleforevent.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final Logger logger = LogManager.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setMaxInactiveInterval(30 * 60); // 30 минут
        logger.debug("Создана сессия: {}", session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.debug("Сессия уничтожена: {}", se.getSession().getId());
    }
}
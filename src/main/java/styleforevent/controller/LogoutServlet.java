package styleforevent.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session != null) {
            String username = "unknown";
            Object user = session.getAttribute("user");
            if (user != null) {
                username = user.toString();
            }

            session.invalidate();
            logger.info("Пользователь вышел из системы: username={}", username);
        } else {
            logger.debug("Попытка выхода без активной сессии");
        }

        resp.sendRedirect("/auth/login");
    }
}
package styleforevent.filter;

import jakarta.servlet.http.HttpFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter extends HttpFilter {
    private static final Logger logger = LogManager.getLogger(AuthFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        String path = req.getRequestURI().substring(req.getContextPath().length());

        logger.trace("Фильтр аутентификации: path={}", path);

        if (path.equals("/auth") || path.startsWith("/auth/") || path.equals("/") || path.contains(".")) {
            logger.trace("Разрешен доступ без аутентификации: path={}", path);
            chain.doFilter(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Доступ запрещен - пользователь не аутентифицирован: path={}", path);
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        logger.trace("Доступ разрешен: path={}", path);
        chain.doFilter(req, resp);
    }
}
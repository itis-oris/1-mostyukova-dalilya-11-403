package styleforevent.controller;

import styleforevent.model.User;
import styleforevent.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AuthServlet.class);
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        logger.info("AuthServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        logger.debug("GET запрос к AuthServlet: path={}", path);

        if (path == null || "/register".equals(path)) {
            logger.debug("Перенаправление на страницу регистрации");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } else if ("/login".equals(path)) {
            logger.debug("Перенаправление на страницу входа");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else {
            logger.warn("Запрошен неизвестный путь: path={}", path);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        logger.debug("POST запрос к AuthServlet: path={}", path);

        if (path == null || "/register".equals(path)) {
            handleRegister(req, resp);
        } else if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else {
            logger.warn("POST запрос к неизвестному пути: path={}", path);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        logger.info("Попытка регистрации: username={}", username);

        if (userService.createUser(username, email, password)) {
            logger.info("Успешная регистрация: username={}", username);
            resp.sendRedirect(req.getContextPath() + "/auth/login");
        } else {
            logger.warn("Ошибка регистрации: username={}", username);
            req.setAttribute("error", "Не удалось зарегистрировать пользователя. Возможно, имя уже занято.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        logger.info("Попытка входа: username={}", username);

        User user = userService.authenticate(username, password);
        if (user != null) {
            logger.info("Успешный вход: username={}, userId={}", username, user.getId());
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            logger.warn("Ошибка входа: username={}", username);
            req.setAttribute("error", "Неверное имя пользователя или пароль");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
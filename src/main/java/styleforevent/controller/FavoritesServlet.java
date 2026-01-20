package styleforevent.controller;

import styleforevent.model.Outfit;
import styleforevent.model.User;
import styleforevent.service.FavoritesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FavoritesServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(FavoritesServlet.class);
    private FavoritesService favoritesService;

    @Override
    public void init() throws ServletException {
        favoritesService = new FavoritesService();
        logger.info("FavoritesServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            logger.warn("Попытка доступа к избранному без авторизации");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        if ("list".equals(action)) {
            showFavorites(req, resp, user);
        } else {
            showFavorites(req, resp, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String outfitIdParam = req.getParameter("outfit_id");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        logger.debug("POST запрос к избранному: action={}, outfitId={}, user={}",
                action, outfitIdParam, user != null ? user.getUsername() : "null");

        resp.setContentType("text/plain");//для джаваскрипта
        resp.setCharacterEncoding("UTF-8");//и это

        if (user == null) {
            logger.warn("Попытка доступа без авторизации: action={}", action);
            resp.getWriter().write("NOT_LOGGED_IN");
            return;
        }

        if (outfitIdParam == null) {
            logger.error("Отсутствует обязательный параметр: outfitId");
            resp.getWriter().write("INVALID_PARAMS");
            return;
        }

        try {
            Long outfitId = Long.parseLong(outfitIdParam);

            if ("add".equals(action)) {
                boolean success = favoritesService.addToFavorites(user.getId(), outfitId);
                logger.info("Результат добавления в избранное: success={}, userId={}, outfitId={}",
                        success, user.getId(), outfitId);

                if (success) {
                    resp.getWriter().write("SUCCESS");
                } else {
                    resp.getWriter().write("ALREADY_FAVORITE");
                }
            } else if ("remove".equals(action)) {
                boolean success = favoritesService.removeFromFavorites(user.getId(), outfitId);
                logger.info("Результат удаления из избранного: success={}, userId={}, outfitId={}",
                        success, user.getId(), outfitId);

                if (success) {
                    resp.getWriter().write("SUCCESS");
                } else {
                    resp.getWriter().write("NOT_IN_FAVORITES");
                }
            } else {
                logger.warn("Неизвестное действие: action={}", action);
                resp.getWriter().write("INVALID_ACTION");
            }
        } catch (NumberFormatException e) {
            logger.error("Неверный формат outfitId: {}", outfitIdParam, e);
            resp.getWriter().write("INVALID_ID");
        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса избранного", e);
            resp.getWriter().write("ERROR");
        }
    }

    private void showFavorites(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        logger.debug("Отображение избранного для пользователя: userId={}", user.getId());

        List<Outfit> favorites = favoritesService.getUserFavorites(user.getId());
        logger.info("Загружено избранных образов: userId={}, count={}", user.getId(), favorites.size());

        req.setAttribute("favorites", favorites);
        req.getRequestDispatcher("/favorites.jsp").forward(req, resp);
    }
}
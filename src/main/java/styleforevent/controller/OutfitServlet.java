package styleforevent.controller;

import styleforevent.model.Outfit;
import styleforevent.model.User;
import styleforevent.service.OutfitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutfitServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(OutfitServlet.class);
    private OutfitService outfitService;

    @Override
    public void init() throws ServletException {
        outfitService = new OutfitService();
        logger.info("OutfitServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        logger.debug("GET запрос к OutfitServlet: action={}, user={}",
                action, user != null ? user.getUsername() : "null");

        if (user == null) {
            logger.warn("Попытка доступа к образам без авторизации");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        if ("create".equals(action)) {
            logger.debug("Отображение формы создания образа: userId={}", user.getId());
            showCreateForm(req, resp, user);
        } else if ("edit".equals(action)) {
            logger.debug("Отображение формы редактирования образа");
            showEditForm(req, resp, user);
        } else if ("delete".equals(action)) {
            logger.info("Удаление образа");
            deleteOutfit(req, resp, user);
        } else {
            logger.debug("Отображение списка образов пользователя: userId={}", user.getId());
            showUserOutfits(req, resp, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            logger.warn("POST запрос к образам без авторизации");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        logger.debug("POST запрос к OutfitServlet: action={}, userId={}", action, user.getId());

        if ("create".equals(action)) {
            createOutfit(req, resp, user);
        } else if ("edit".equals(action)) {
            updateOutfit(req, resp, user);
        } else if ("deleteImage".equals(action)) {
            deleteImage(req, resp, user);
        }
    }

    private void showUserOutfits(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        logger.debug("Получение образов пользователя: userId={}", user.getId());
        List<Outfit> outfits = outfitService.findByUserId(user.getId());
        logger.info("Загружено образов пользователя: userId={}, count={}", user.getId(), outfits.size());

        req.setAttribute("outfits", outfits);
        req.getRequestDispatcher("/my-outfits.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        logger.debug("Отображение формы создания образа");
        req.getRequestDispatcher("/create-outfit.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        String idParam = req.getParameter("id");
        logger.debug("Отображение формы редактирования: outfitId={}", idParam);

        try {
            Long id = Long.parseLong(idParam);
            Outfit outfit = outfitService.findById(id);

            if (outfit != null && outfit.getUserId().equals(user.getId())) {
                logger.debug("Разрешение на редактирование: outfitId={}, userId={}", id, user.getId());
                req.setAttribute("outfit", outfit);
                req.getRequestDispatcher("/edit-outfit.jsp").forward(req, resp);
            } else {
                logger.warn("Отказ в доступе к редактированию: outfitId={}, userId={}", id, user.getId());
                resp.sendRedirect(req.getContextPath() + "/outfits");
            }
        } catch (NumberFormatException e) {
            logger.error("Неверный формат ID образа: {}", idParam, e);
            resp.sendRedirect(req.getContextPath() + "/outfits");
        }
    }

    private void createOutfit(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String eventIdParam = req.getParameter("event_id");
        String gender = req.getParameter("gender");
        String[] imageUrls = req.getParameterValues("image_urls");

        logger.info("Создание образа: name={}, eventId={}, gender={}, imagesCount={}",
                name, eventIdParam, gender, imageUrls != null ? imageUrls.length : 0);

        if (name == null || name.trim().isEmpty() || eventIdParam == null || gender == null) {
            logger.warn("Не заполнены обязательные поля при создании образа");
            req.setAttribute("error", "Заполните обязательные поля");
            req.getRequestDispatcher("/create-outfit.jsp").forward(req, resp);
            return;
        }

        try {
            Long eventId = Long.parseLong(eventIdParam);

            Outfit outfit = new Outfit();
            outfit.setName(name.trim());
            outfit.setDescription(description != null ? description.trim() : null);
            outfit.setUserId(user.getId());
            outfit.setEventId(eventId);
            outfit.setGender(gender);

            List<String> imageUrlList = new ArrayList<>();
            if (imageUrls != null) {
                for (String url : imageUrls) {
                    if (url != null && !url.trim().isEmpty()) {
                        imageUrlList.add(url.trim());
                    }
                }
            }

            logger.debug("Сохранение образа в БД: name={}, userId={}", outfit.getName(), user.getId());
            boolean success = outfitService.createOutfit(outfit, imageUrlList);
            logger.info("Результат создания образа: success={}, outfitId={}", success, outfit.getId());

            if (success) {
                logger.info("Успешное создание образа: outfitId={}", outfit.getId());
                resp.sendRedirect(req.getContextPath() + "/outfits");
            } else {
                logger.error("Ошибка создания образа: name={}", outfit.getName());
                req.setAttribute("error", "Не удалось создать образ");
                req.getRequestDispatcher("/create-outfit.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            logger.error("Неверный формат eventId: {}", eventIdParam, e);
            req.setAttribute("error", "Неверный формат мероприятия");
            req.getRequestDispatcher("/create-outfit.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при создании образа", e);
            req.setAttribute("error", "Произошла ошибка: " + e.getMessage());
            req.getRequestDispatcher("/create-outfit.jsp").forward(req, resp);
        }
    }

    private void updateOutfit(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException, ServletException {
        String idParam = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String eventIdParam = req.getParameter("event_id");
        String gender = req.getParameter("gender");
        String[] newImageUrls = req.getParameterValues("image_urls");

        logger.info("Обновление образа: outfitId={}, name={}, eventId={}", idParam, name, eventIdParam);

        try {
            Long id = Long.parseLong(idParam);
            Long eventId = Long.parseLong(eventIdParam);

            Outfit outfit = outfitService.findById(id);

            if (outfit == null || !outfit.getUserId().equals(user.getId())) {
                logger.warn("Отказ в доступе к обновлению: outfitId={}, userId={}", id, user.getId());
                resp.sendRedirect(req.getContextPath() + "/outfits");
                return;
            }

            outfit.setName(name.trim());
            outfit.setDescription(description != null ? description.trim() : null);
            outfit.setEventId(eventId);
            outfit.setGender(gender);

            logger.debug("Обновление данных образа: outfitId={}", outfit.getId());
            boolean success = outfitService.updateOutfit(outfit);
            logger.info("Результат обновления образа: success={}, outfitId={}", success, outfit.getId());

            if (newImageUrls != null && newImageUrls.length > 0) {
                List<String> validImageUrls = new ArrayList<>();
                for (String url : newImageUrls) {
                    if (url != null && !url.trim().isEmpty()) {
                        validImageUrls.add(url.trim());
                    }
                }
                if (!validImageUrls.isEmpty()) {
                    logger.debug("Добавление новых изображений: outfitId={}, count={}", outfit.getId(), validImageUrls.size());
                    outfitService.addImagesToOutfit(outfit.getId(), validImageUrls);
                }
            }

            if (success) {
                logger.info("Успешное обновление образа: outfitId={}", outfit.getId());
                resp.sendRedirect(req.getContextPath() + "/outfits");
            } else {
                logger.error("Ошибка обновления образа: outfitId={}", outfit.getId());
                req.setAttribute("error", "Не удалось обновить образ");
                req.setAttribute("outfit", outfit);
                req.getRequestDispatcher("/edit-outfit.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            logger.error("Неверный формат данных при обновлении образа", e);
            req.setAttribute("error", "Неверный формат данных");
            req.getRequestDispatcher("/edit-outfit.jsp").forward(req, resp);
        }
    }

    private void deleteOutfit(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String idParam = req.getParameter("id");
        logger.info("Удаление образа: outfitId={}", idParam);

        try {
            Long id = Long.parseLong(idParam);
            Outfit outfit = outfitService.findById(id);

            if (outfit != null && outfit.getUserId().equals(user.getId())) {
                logger.debug("Удаление образа с проверкой прав: outfitId={}, userId={}", id, user.getId());
                boolean deleted = outfitService.deleteOutfit(id);
                logger.info("Результат удаления образа: success={}, outfitId={}", deleted, id);
            } else {
                logger.warn("Отказ в удалении образа: outfitId={}, userId={}", id, user.getId());
            }
            resp.sendRedirect(req.getContextPath() + "/outfits");
        } catch (NumberFormatException e) {
            logger.error("Неверный формат outfitId: {}", idParam, e);
            resp.sendRedirect(req.getContextPath() + "/outfits");
        }
    }

    private void deleteImage(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        String imageIdParam = req.getParameter("image_id");
        logger.info("Удаление изображения: imageId={}", imageIdParam);

        try {
            Long imageId = Long.parseLong(imageIdParam);

            boolean success = outfitService.deleteImage(imageId);
            logger.info("Результат удаления изображения: success={}, imageId={}", success, imageId);

            resp.setContentType("text/plain");
            resp.setCharacterEncoding("UTF-8");

            if (success) {
                resp.getWriter().write("SUCCESS");
            } else {
                resp.getWriter().write("ERROR");
            }
        } catch (NumberFormatException e) {
            logger.error("Неверный формат imageId: {}", imageIdParam, e);
            resp.getWriter().write("INVALID_ID");
        }
    }
}
package styleforevent.controller;

import styleforevent.model.Outfit;
import styleforevent.service.OutfitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OutfitCatalogServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(OutfitCatalogServlet.class);
    private OutfitService outfitService;

    @Override
    public void init() throws ServletException {
        outfitService = new OutfitService();
        logger.info("OutfitCatalogServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eventIdParam = req.getParameter("event_id");
        String gender = req.getParameter("gender");

        logger.debug("Запрос каталога образов: eventId={}, gender={}", eventIdParam, gender);

        if (eventIdParam == null || gender == null) {
            logger.warn("Отсутствуют параметры фильтрации каталога");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Long eventId = Long.parseLong(eventIdParam);
            List<Outfit> outfits = outfitService.findByEventAndGender(eventId, gender);

            logger.info("Найдено образов в каталоге: eventId={}, gender={}, count={}",
                    eventId, gender, outfits.size());

            req.setAttribute("outfits", outfits);
            req.setAttribute("eventId", eventIdParam);
            req.setAttribute("gender", gender);
            req.setAttribute("genderDisplay", "FEMALE".equals(gender) ? "Женские" : "Мужские");

            req.getRequestDispatcher("/outfit-catalog.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            logger.error("Неверный формат eventId: {}", eventIdParam, e);
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
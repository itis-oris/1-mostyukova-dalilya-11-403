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

public class ViewOutfitServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(ViewOutfitServlet.class);
    private OutfitService outfitService;

    @Override
    public void init() throws ServletException {
        outfitService = new OutfitService();
        logger.info("ViewOutfitServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        logger.debug("Просмотр образа: outfitId={}", idParam);

        if (idParam == null) {
            logger.warn("Попытка просмотра образа без указания ID");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Long outfitId = Long.parseLong(idParam);
            Outfit outfit = outfitService.findById(outfitId);

            if (outfit == null) {
                logger.warn("Образ не найден: outfitId={}", outfitId);
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            logger.info("Отображение образа: outfitId={}, name={}, imagesCount={}",
                    outfitId, outfit.getName(), outfit.getImages().size());

            req.setAttribute("outfit", outfit);
            req.getRequestDispatcher("/view-outfit.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            logger.error("Неверный формат outfitId: {}", idParam, e);
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
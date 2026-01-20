package styleforevent.controller;

import styleforevent.model.Event;
import styleforevent.service.EventService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HomeServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(HomeServlet.class);
    private EventService eventService;

    @Override
    public void init() throws ServletException {
        eventService = new EventService();
        logger.info("HomeServlet инициализирован");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Обработка запроса главной страницы");

        List<Event> events = eventService.findAllEvents();
        logger.debug("Загружено мероприятий для главной страницы: count={}", events.size());

        req.setAttribute("events", events);
        req.getRequestDispatcher("/home.jsp").forward(req, resp);

        logger.trace("Главная страница успешно отображена");
    }
}
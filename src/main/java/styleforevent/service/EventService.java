package styleforevent.service;

import styleforevent.dao.EventDao;
import styleforevent.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class EventService {
    private static final Logger logger = LogManager.getLogger(EventService.class);
    private EventDao eventDao;

    public EventService() {
        eventDao = new EventDao();
    }

    public List<Event> findAllEvents() {
        logger.debug("Получение списка мероприятий");

        List<Event> events = eventDao.findAll();
        logger.info("Загружено мероприятий: count={}", events.size());

        return events;
    }
}
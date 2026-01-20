package styleforevent.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {
    private static final Logger logger = LogManager.getLogger(RequestListener.class);

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        if (sre.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
            request.setAttribute("requestStartTime", System.currentTimeMillis());
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        if (sre.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
            Long startTime = (Long) request.getAttribute("requestStartTime");
            if (startTime != null) {
                long time = System.currentTimeMillis() - startTime;
                if (time > 1000) { //1 сек
                    logger.warn("Медленный запрос: {} {} - {} мс",
                            request.getMethod(), request.getRequestURI(), time);
                }
            }
        }
    }
}
package styleforevent.service;

import styleforevent.dao.UserDao;
import styleforevent.model.User;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private UserDao userDao;

    public UserService() {
        userDao = new UserDao();
    }

    public boolean createUser(String username, String email, String password) {
        logger.debug("Попытка создания пользователя: username={}", username);

        if (userDao.findByUsername(username).isPresent()) {
            logger.warn("Пользователь уже существует: username={}", username);
            return false;
        }

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);

        boolean result = userDao.createUser(user);
        logger.info("Результат создания пользователя: success={}, username={}", result, username);
        return result;
    }

    public User authenticate(String username, String password) {
        logger.debug("Аутентификация пользователя: username={}", username);

        return userDao.findByUsername(username)
                .map(user -> {
                    if (BCrypt.verifyer().verify(password.toCharArray(), user.getPasswordHash()).verified) {
                        logger.info("Успешная аутентификация: username={}", username);
                        return user;
                    }
                    logger.warn("Неверный пароль: username={}", username);
                    return null;
                })
                .orElseGet(() -> {
                    logger.warn("Пользователь не найден: username={}", username);
                    return null;
                });
    }
}
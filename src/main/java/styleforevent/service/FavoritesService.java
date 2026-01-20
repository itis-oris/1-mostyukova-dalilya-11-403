package styleforevent.service;

import styleforevent.dao.FavoritesDao;
import styleforevent.model.Outfit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class FavoritesService {
    private static final Logger logger = LogManager.getLogger(FavoritesService.class);
    private FavoritesDao favoritesDao;
    private OutfitService outfitService;

    public FavoritesService() {
        favoritesDao = new FavoritesDao();
        outfitService = new OutfitService();
    }

    public boolean addToFavorites(Long userId, Long outfitId) {
        logger.info("Добавление в избранное: userId={}, outfitId={}", userId, outfitId);

        Outfit outfit = outfitService.findById(outfitId);
        if (outfit == null) {
            logger.warn("Образ не найден: outfitId={}", outfitId);
            return false;
        }

        if (favoritesDao.isFavorite(userId, outfitId)) {
            logger.debug("Образ уже в избранном: outfitId={}", outfitId);
            return true;
        }

        boolean success = favoritesDao.addToFavorites(userId, outfitId);
        logger.info("Результат добавления в избранное: success={}, outfitId={}", success, outfitId);
        return success;
    }

    public boolean removeFromFavorites(Long userId, Long outfitId) {
        logger.info("Удаление из избранного: userId={}, outfitId={}", userId, outfitId);

        boolean success = favoritesDao.removeFromFavorites(userId, outfitId);
        logger.info("Результат удаления из избранного: success={}, outfitId={}", success, outfitId);
        return success;
    }

    public List<Outfit> getUserFavorites(Long userId) {
        logger.debug("Получение избранного пользователя: userId={}", userId);

        List<Long> favoriteOutfitIds = favoritesDao.getUserFavoriteIds(userId);
        logger.debug("Найдено ID избранных образов: userId={}, count={}", userId, favoriteOutfitIds.size());

        List<Outfit> favorites = outfitService.findByIds(favoriteOutfitIds);
        logger.info("Загружено избранных образов: userId={}, count={}", userId, favorites.size());

        return favorites;
    }

    public boolean isFavorite(Long userId, Long outfitId) {
        logger.trace("Проверка избранного: userId={}, outfitId={}", userId, outfitId);
        return favoritesDao.isFavorite(userId, outfitId);
    }
}
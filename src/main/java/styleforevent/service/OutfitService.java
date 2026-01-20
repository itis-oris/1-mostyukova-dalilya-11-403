package styleforevent.service;

import styleforevent.dao.OutfitDao;
import styleforevent.dao.OutfitImageDao;
import styleforevent.model.Outfit;
import styleforevent.model.OutfitImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class OutfitService {
    private static final Logger logger = LogManager.getLogger(OutfitService.class);
    private OutfitDao outfitDao;
    private OutfitImageDao imageDao;

    public OutfitService() {
        outfitDao = new OutfitDao();
        imageDao = new OutfitImageDao();
    }

    public boolean createOutfit(Outfit outfit, List<String> imageUrls) {
        logger.info("Создание образа: name={}, userId={}, eventId={}",
                outfit.getName(), outfit.getUserId(), outfit.getEventId());

        boolean outfitCreated = outfitDao.create(outfit);
        logger.debug("Результат создания образа в БД: success={}, outfitId={}",
                outfitCreated, outfit.getId());

        if (!outfitCreated) return false;

        if (imageUrls != null && !imageUrls.isEmpty()) {
            int savedImages = 0;
            for (String imageUrl : imageUrls) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    OutfitImage image = new OutfitImage();
                    image.setOutfitId(outfit.getId());
                    image.setImageUrl(imageUrl.trim());
                    boolean imageCreated = imageDao.create(image);
                    if (imageCreated) savedImages++;
                    logger.trace("Сохранение изображения: success={}, url={}", imageCreated, imageUrl);
                }
            }
            logger.info("Сохранено изображений: {}/{}", savedImages, imageUrls.size());
        }
        return true;
    }

    public List<Outfit> findByUserId(Long userId) {
        logger.debug("Поиск образов пользователя: userId={}", userId);

        List<Outfit> outfits = outfitDao.findByUserId(userId);

        for (Outfit outfit : outfits) {
            List<OutfitImage> images = imageDao.findByOutfitId(outfit.getId());
            outfit.setImages(images);
            logger.trace("Образ: name={}, imagesCount={}", outfit.getName(), images.size());
        }

        logger.info("Найдено образов для пользователя: userId={}, count={}", userId, outfits.size());
        return outfits;
    }

    public Outfit findById(Long id) {
        logger.debug("Поиск образа по ID: outfitId={}", id);

        Outfit outfit = outfitDao.findById(id);
        if (outfit != null) {
            List<OutfitImage> images = imageDao.findByOutfitId(outfit.getId());
            outfit.setImages(images);
            logger.debug("Образ найден: name={}, imagesCount={}", outfit.getName(), images.size());
        } else {
            logger.warn("Образ не найден: outfitId={}", id);
        }
        return outfit;
    }

    public boolean updateOutfit(Outfit outfit) {
        logger.info("Обновление образа: outfitId={}, name={}", outfit.getId(), outfit.getName());

        boolean result = outfitDao.update(outfit);
        logger.debug("Результат обновления образа: success={}", result);
        return result;
    }

    public boolean deleteOutfit(Long id) {
        logger.info("Удаление образа: outfitId={}", id);

        imageDao.deleteByOutfitId(id);
        boolean result = outfitDao.delete(id);
        logger.info("Результат удаления образа: success={}, outfitId={}", result, id);
        return result;
    }

    public boolean addImagesToOutfit(Long outfitId, List<String> imageUrls) {
        logger.info("Добавление изображений к образу: outfitId={}, imagesCount={}", outfitId, imageUrls.size());

        if (imageUrls == null || imageUrls.isEmpty()) {
            logger.warn("Пустой список изображений для добавления: outfitId={}", outfitId);
            return false;
        }

        boolean allSuccess = true;
        int savedCount = 0;
        for (String imageUrl : imageUrls) {
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                OutfitImage image = new OutfitImage();
                image.setOutfitId(outfitId);
                image.setImageUrl(imageUrl.trim());
                boolean success = imageDao.create(image);
                if (success) savedCount++;
                if (!success) allSuccess = false;
            }
        }

        logger.info("Добавлено изображений: {}/{}, success={}", savedCount, imageUrls.size(), allSuccess);
        return allSuccess;
    }

    public boolean deleteImage(Long imageId) {
        logger.info("Удаление изображения: imageId={}", imageId);

        boolean result = imageDao.delete(imageId);
        logger.debug("Результат удаления изображения: success={}, imageId={}", result, imageId);
        return result;
    }

    public List<Outfit> findByEventAndGender(Long eventId, String gender) {
        logger.debug("Поиск образов по фильтрам: eventId={}, gender={}", eventId, gender);

        List<Outfit> outfits = outfitDao.findByEventAndGender(eventId, gender);

        for (Outfit outfit : outfits) {
            List<OutfitImage> images = imageDao.findByOutfitId(outfit.getId());
            outfit.setImages(images);
        }

        logger.info("Найдено образов по фильтру: eventId={}, gender={}, count={}",
                eventId, gender, outfits.size());
        return outfits;
    }

    public List<Outfit> findByIds(List<Long> ids) {
        logger.debug("Поиск образов по списку ID: idsCount={}", ids.size());

        if (ids == null || ids.isEmpty()) {
            logger.warn("Пустой список ID для поиска образов");
            return new ArrayList<>();
        }

        List<Outfit> outfits = new ArrayList<>();
        for (Long id : ids) {
            Outfit outfit = findById(id);
            if (outfit != null) {
                outfits.add(outfit);
            }
        }

        logger.debug("Найдено образов из списка: {}/{}", outfits.size(), ids.size());
        return outfits;
    }
}
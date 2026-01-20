package styleforevent.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Outfit {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long userId;
    private Long eventId;
    private String gender;
    private LocalDateTime createdAt;
    private List<OutfitImage> images = new ArrayList<>();

    public Outfit() {
    }

    public Outfit(Long id, LocalDateTime createdAt, String gender, Long eventId, Long userId, String imageUrl, String description, String name, List<OutfitImage> images) {
        this.id = id;
        this.createdAt = createdAt;
        this.gender = gender;
        this.eventId = eventId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.name = name;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OutfitImage> getImages() {
        return images;
    }

    public void setImages(List<OutfitImage> images) {
        this.images = images;
    }

    public void addImage(OutfitImage image) {
        this.images.add(image);
    }
}

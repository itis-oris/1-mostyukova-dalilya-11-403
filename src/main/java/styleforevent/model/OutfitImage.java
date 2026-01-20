package styleforevent.model;

import java.time.LocalDateTime;

public class OutfitImage {
    private Long id;
    private Long outfitId;
    private String imageUrl;
    private LocalDateTime createdAt;

    public OutfitImage() {}

    public OutfitImage(Long id, Long outfitId, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.outfitId = outfitId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOutfitId() { return outfitId; }
    public void setOutfitId(Long outfitId) { this.outfitId = outfitId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
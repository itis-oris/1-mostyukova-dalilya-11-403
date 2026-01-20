package styleforevent.model;


import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String passwordHash, LocalDateTime createdAt, String email, String username, Long id) {
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.email = email;
        this.username = username;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
package com.shopflow.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 512)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    private boolean revoked = false;

    public RefreshToken() { this.revoked = false; }

    public RefreshToken(Long id, String token, User user, LocalDateTime expiryDate, boolean revoked) {
        this.id = id; this.token = token; this.user = user;
        this.expiryDate = expiryDate; this.revoked = revoked;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id; private String token; private User user;
        private LocalDateTime expiryDate; private boolean revoked = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder expiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder revoked(boolean revoked) { this.revoked = revoked; return this; }

        public RefreshToken build() { return new RefreshToken(id, token, user, expiryDate, revoked); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((RefreshToken) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

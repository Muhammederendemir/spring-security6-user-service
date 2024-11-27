
package com.med.springsecurity6.refleshtoken;

import com.med.springsecurity6.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "REFRESH_TOKENS")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Private Constructor for Builder
    private RefreshToken(Builder builder) {
        this.id = builder.id;
        this.token = builder.token;
        this.expiryDate = builder.expiryDate;
        this.user = builder.user;
    }
    // Public No-Args Constructor (Required by JPA)
    public RefreshToken() {}

    public static class Builder {
        private int id;
        private String token;
        private Instant expiryDate;
        private User user;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setExpiryDate(Instant expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public RefreshToken build() {
            return new RefreshToken(this);
        }
    }
}



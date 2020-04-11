package com.mefollow.webschool.management.user.domain.auth;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;

@Document(collection = "verification-tokens")
public class VerificationToken extends BaseModel {
    private String userId;
    private String token;
    private LocalDateTime expireAt;

    public VerificationToken(String userId, String token, Duration lifetime) {
        this.userId = userId;
        this.token = token;
        this.expireAt = LocalDateTime.now().plus(lifetime);
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
}

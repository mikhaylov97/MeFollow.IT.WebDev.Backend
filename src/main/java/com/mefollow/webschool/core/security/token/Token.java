package com.mefollow.webschool.core.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mefollow.webschool.core.domain.BaseModel;
import com.mefollow.webschool.management.user.domain.account.User;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;

@Document(collection = "tokens")
public class Token extends BaseModel {

    @JsonIgnore
    private String userId;
    private TokenType type;
    @Indexed(unique = true)
    private String token;
    private LocalDateTime expireAt;

    public Token() {
    }

    public Token(String userID, TokenType type) {
        this.userId = userID;
        this.type = type;
        this.token = randomUUID().toString();
        this.expireAt = now().plus(type.getLifetime());
    }

    public Token(String userID, TokenType type, String token) {
        this.userId = userID;
        this.type = type;
        this.token = token;
        this.expireAt = now().plus(type.getLifetime());
    }

    public static Token accessTokenFor(User user) {
        return new Token(user.getId(), TokenType.ACCESS);
    }

    public static Mono<List<Token>> tokensFor(User user) {
        return tokensFor(user.getId());
    }

    public static Mono<List<Token>> tokensFor(String userId) {
        return Mono.just(asList(new Token(userId, TokenType.ACCESS), new Token(userId, TokenType.REFRESH)));
    }

    @Override
    @JsonIgnore
    public String getId() {
        return super.getId();
    }

    public String getUserId() {
        return userId;
    }

    public TokenType getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }
}

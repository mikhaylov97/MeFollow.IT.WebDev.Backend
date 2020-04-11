package com.mefollow.webschool.core.security.token;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;

public enum TokenType {
    ACCESS(60 * 24 * 7),
    REFRESH(60 * 24 * 7);

    private int lifetime;

    TokenType(int lifetime) {
        this.lifetime = lifetime;
    }

    public Duration getLifetime() {
        return Duration.of(lifetime, MINUTES);
    }
}

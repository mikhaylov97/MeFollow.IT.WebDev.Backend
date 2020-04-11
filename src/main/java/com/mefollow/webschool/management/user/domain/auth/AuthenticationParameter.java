package com.mefollow.webschool.management.user.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "authentication-parameters")
public abstract class AuthenticationParameter extends BaseModel {

    private String userId;

    @JsonIgnore
    private AuthenticationProvider authenticationProvider;

    public AuthenticationParameter(String userId, AuthenticationProvider authenticationProvider) {
        this.userId = userId;
        this.authenticationProvider = authenticationProvider;
    }

    public String getUserId() {
        return userId;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    @Override
    public String toString() {
        return "AuthenticationParameter{" +
                "id='" + id + '\'' +
                "userId='" + userId + '\'' +
                "authenticationProvider='" + authenticationProvider +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationParameter that = (AuthenticationParameter) o;
        return Objects.equals(userId, that.userId) &&
                authenticationProvider == that.authenticationProvider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, authenticationProvider);
    }
}

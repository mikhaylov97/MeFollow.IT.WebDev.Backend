package com.mefollow.webschool.management.user.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static com.mefollow.webschool.management.user.domain.auth.AuthenticationProvider.PASSWORD;

public class DirectAuthenticationParameter extends AuthenticationParameter {

    @JsonIgnore
    private String password;

    public DirectAuthenticationParameter(String userId, String password) {
        super(userId, PASSWORD);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

package com.mefollow.webschool.management.user.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IndirectAuthenticationParameter extends AuthenticationParameter {

    @JsonIgnore
    private String externalId;

    public IndirectAuthenticationParameter(String userId, AuthenticationProvider authenticationProvider, String externalId) {
        super(userId, authenticationProvider);
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }
}

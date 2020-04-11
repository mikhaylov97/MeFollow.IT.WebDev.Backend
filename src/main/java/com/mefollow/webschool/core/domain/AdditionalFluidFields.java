package com.mefollow.webschool.core.domain;

public enum AdditionalFluidFields {
    DEFAULT_PASSWORD("defaultPassword"),
    TOKENS("tokens"),
    VERIFICATION_TOKEN("verificationToken");

    private final String fieldName;

    AdditionalFluidFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

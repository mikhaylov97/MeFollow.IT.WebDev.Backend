package com.mefollow.webschool.core.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.mefollow.webschool.core.exception.ApiError.ApiErrorBuilder.newApiError;
import static java.util.Objects.nonNull;

@JsonInclude(NON_NULL)
public class ApiError {
    private final String defaultMessage;
    private final String field;

    public ApiError(String defaultMessage, String field) {
        this.defaultMessage = defaultMessage;
        this.field = field;
    }

    public static ApiError errorWith(String defaultMessage) {
        return newApiError().withDefaultMessage(defaultMessage).build();
    }

    public static ApiError errorWith(String defaultMessage, String field) {
        return newApiError().withDefaultMessage(defaultMessage).withField(field).build();
    }

    public static ApiError errorEmpty() {
        return newApiError().build();
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getField() {
        return field;
    }

    public boolean notEmpty() {
        return nonNull(defaultMessage) || nonNull(field);
    }

    public static final class ApiErrorBuilder {
        private String defaultMessage;
        private String field;

        private ApiErrorBuilder() {
        }

        public static ApiErrorBuilder newApiError() {
            return new ApiErrorBuilder();
        }

        public ApiErrorBuilder withDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
            return this;
        }

        public ApiErrorBuilder withField(String field) {
            this.field = field;
            return this;
        }

        public ApiError build() {
            return new ApiError(defaultMessage, field);
        }
    }
}

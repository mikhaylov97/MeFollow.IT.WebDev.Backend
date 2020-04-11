package com.mefollow.webschool.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@JsonInclude(NON_NULL)
public class CoreException extends Exception {
    protected final List<ApiError> errors;
    protected final HttpStatus status;

    public CoreException(HttpStatus status, ApiError... error) {
        this(stream(error).map(ApiError::getDefaultMessage).filter(Objects::nonNull).collect(joining("; ")),
                status, error);
    }

    private CoreException(String message, HttpStatus status, ApiError... error) {
        super(message);
        this.status = status;
        this.errors = stream(error).filter(ApiError::notEmpty).collect(toList());
    }

    public String getError() {
        return status.getReasonPhrase();
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public int getStatus() {
        return status.value();
    }

    public Date getTimestamp() {
        return new Date();
    }

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        return status;
    }

    @JsonIgnore
    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}

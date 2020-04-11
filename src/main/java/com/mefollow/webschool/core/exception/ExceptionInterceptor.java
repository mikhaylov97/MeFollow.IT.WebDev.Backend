package com.mefollow.webschool.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

public abstract class ExceptionInterceptor {

    @ExceptionHandler(CoreException.class)
    ResponseEntity<Mono<?>> handleCoreException(CoreException e) {
        return new ResponseEntity<>(just(e), buildHeaders(), e.getHttpStatus());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<Mono<?>> handleWebExchangeBindException(WebExchangeBindException ex) {
        return new ResponseEntity<>(just(new CoreException(ex.getStatus(), buildErrors(ex))), buildHeaders(), ex.getStatus());
    }

    private ApiError[] buildErrors(WebExchangeBindException ex) {
        return ex.getAllErrors().stream()
                .map(this::buildApiError)
                .collect(toList())
                .toArray(new ApiError[]{});
    }

    private ApiError buildApiError(ObjectError error) {
        ApiError.ApiErrorBuilder builder = ApiError.ApiErrorBuilder.newApiError();
        builder.withDefaultMessage(error.getDefaultMessage());
        if (error instanceof FieldError) {
            builder.withField(((FieldError)error).getField());
        }
        return builder.build();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}

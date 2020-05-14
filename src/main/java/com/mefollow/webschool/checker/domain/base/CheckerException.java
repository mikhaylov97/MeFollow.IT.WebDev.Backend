package com.mefollow.webschool.checker.domain.base;

import com.mefollow.webschool.core.exception.ApiError;
import com.mefollow.webschool.core.exception.CoreException;
import org.springframework.http.HttpStatus;

import static com.mefollow.webschool.core.exception.ApiError.errorWith;
import static org.springframework.http.HttpStatus.*;

public class CheckerException extends CoreException {
    private static final String EXCEPTION_BASE_CODE = "errors.backend.resource.";

    public static final CheckerException HTML_CHECK_RULES_CANNOT_BE_EMPTY = conflict("htmlCheckRulesCannotBeEmpty");

    private CheckerException(HttpStatus status, ApiError... error) {
        super(status, error);
    }

    public static CheckerException forbidden(String target) {
        return new CheckerException(FORBIDDEN, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static CheckerException conflict(String target, String fieldName) {
        return new CheckerException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    public static CheckerException conflict(String target) {
        return new CheckerException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target));
    }

    private static CheckerException notFound(String target) {
        return new CheckerException(NOT_FOUND, errorWith(EXCEPTION_BASE_CODE + target + "NotFound"));
    }
}

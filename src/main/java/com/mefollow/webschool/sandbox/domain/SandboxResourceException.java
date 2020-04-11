package com.mefollow.webschool.sandbox.domain;

import com.mefollow.webschool.core.exception.ApiError;
import com.mefollow.webschool.core.exception.CoreException;
import org.springframework.http.HttpStatus;

import static com.mefollow.webschool.core.exception.ApiError.errorWith;
import static org.springframework.http.HttpStatus.*;

public class SandboxResourceException extends CoreException {

    private static final String EXCEPTION_BASE_CODE = "errors.backend.resource.";

    public static final SandboxResourceException SANDBOX_RESOURCE_NOT_FOUND = notFound("sandboxResource");
    public static final SandboxResourceException SANDBOX_RESOURCE_FORBIDDEN = forbidden("sandboxResourceForbidden");
    public static final SandboxResourceException WRONG_SANDBOX_RESOURCE_TYPE = conflict("wrongSandboxResourceType", "type");

    private SandboxResourceException(HttpStatus status, ApiError... error) {
        super(status, error);
    }

    public static SandboxResourceException forbidden(String target) {
        return new SandboxResourceException(FORBIDDEN, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static SandboxResourceException conflict(String target, String fieldName) {
        return new SandboxResourceException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    private static SandboxResourceException notFound(String target) {
        return new SandboxResourceException(NOT_FOUND, errorWith(EXCEPTION_BASE_CODE + target + "NotFound"));
    }
}

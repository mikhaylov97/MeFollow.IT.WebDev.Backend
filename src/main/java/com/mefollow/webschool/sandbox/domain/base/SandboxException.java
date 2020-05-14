package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.exception.ApiError;
import com.mefollow.webschool.core.exception.CoreException;
import org.springframework.http.HttpStatus;

import static com.mefollow.webschool.core.exception.ApiError.errorWith;
import static org.springframework.http.HttpStatus.*;

public class SandboxException extends CoreException {

    private static final String EXCEPTION_BASE_CODE = "errors.backend.resource.";

    public static final SandboxException PROGRESS_NOT_FOUND = notFound("progress");
    public static final SandboxException CHAPTER_NOT_FOUND = notFound("chapter");
    public static final SandboxException LESSON_NOT_FOUND = notFound("lesson");
    public static final SandboxException TOPIC_NOT_FOUND = notFound("topic");
    public static final SandboxException COURSE_NOT_FOUND = notFound("course");
    public static final SandboxException SANDBOX_RESOURCE_NOT_FOUND = notFound("sandboxResource");
    public static final SandboxException SANDBOX_RESOURCE_BUNDLE_NOT_FOUND = notFound("sandboxResourceBundle");
    public static final SandboxException SANDBOX_RESOURCE_FORBIDDEN = forbidden("sandboxResourceForbidden");
    public static final SandboxException SANDBOX_RESOURCE_BUNDLE_FORBIDDEN = forbidden("sandboxResourceBundleForbidden");
    public static final SandboxException CHAPTER_IS_NOT_SOLVED = conflict("chapterIsNotSolved");
    public static final SandboxException SANDBOX_RESOURCES_INCONSISTENCY = conflict("sandboxResourcesInconsistency");
    public static final SandboxException WRONG_SANDBOX_RESOURCE_TYPE = conflict("wrongSandboxResourceType", "type");

    private SandboxException(HttpStatus status, ApiError... error) {
        super(status, error);
    }

    public static SandboxException forbidden(String target) {
        return new SandboxException(FORBIDDEN, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static SandboxException conflict(String target, String fieldName) {
        return new SandboxException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    public static SandboxException conflict(String target) {
        return new SandboxException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target));
    }

    private static SandboxException notFound(String target) {
        return new SandboxException(NOT_FOUND, errorWith(EXCEPTION_BASE_CODE + target + "NotFound"));
    }
}

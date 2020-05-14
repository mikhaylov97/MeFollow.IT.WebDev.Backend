package com.mefollow.webschool.management.admin.domain.base;

import com.mefollow.webschool.core.exception.ApiError;
import com.mefollow.webschool.core.exception.CoreException;
import org.springframework.http.HttpStatus;

import static com.mefollow.webschool.core.exception.ApiError.errorWith;
import static org.springframework.http.HttpStatus.*;

public class ManagementException extends CoreException {
    private static final String EXCEPTION_BASE_CODE = "errors.backend.management.";

    public static final ManagementException ACTION_IS_FORBIDDEN = forbidden("actionIsForbidden");
    public static final ManagementException WRONG_RULE_TYPE = conflict("wrongRuleType");
    public static final ManagementException HTML_RULE_WITH_SUCH_CSS_SELECTOR_ALREADY_EXISTS = conflict("htmlRuleWithSuchCssSelectorAlreadyExists");
    public static final ManagementException CSS_RULE_WITH_SUCH_HTML_RULE_REFERENCE_ALREADY_EXISTS = conflict("cssRuleWithSuchHtmlRuleReferenceAlreadyExists");
    public static final ManagementException JS_RULE_ALREADY_EXISTS = conflict("jsRuleAlreadyExists");
    public static final ManagementException PAYLOAD_DATA_INCONSISTENCY_HTML_RULE_CSS_SELECTOR = conflict("payloadDataInconsistency", "htmlRuleCssSelector");
    public static final ManagementException PAYLOAD_DATA_INCONSISTENCY_HTML_RULE_TAG_NAME = conflict("payloadDataInconsistency", "htmlRuleTagName");
    public static final ManagementException PAYLOAD_DATA_INCONSISTENCY_CSS_RULE_TAG_REFERENCE = conflict("payloadDataInconsistency", "cssRuleHtmlRuleReference");
    public static final ManagementException PAYLOAD_DATA_INCONSISTENCY_JS_RULE_EXECUTABLE = conflict("payloadDataInconsistency", "jsRuleExecutable");
    public static final ManagementException PAYLOAD_DATA_INCONSISTENCY_JS_RULE_CONTENT = conflict("payloadDataInconsistency", "jsRuleContent");
    public static final ManagementException TOPIC_ALREADY_EXISTS = conflict("topicAlreadyExists");
    public static final ManagementException LESSON_ALREADY_EXISTS = conflict("lessonAlreadyExists");
    public static final ManagementException CHAPTER_ALREADY_EXISTS = conflict("chapterAlreadyExists");

    private ManagementException(HttpStatus status, ApiError... error) {
        super(status, error);
    }

    public static ManagementException forbidden(String target) {
        return new ManagementException(FORBIDDEN, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static ManagementException conflict(String target, String fieldName) {
        return new ManagementException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    public static ManagementException conflict(String target) {
        return new ManagementException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target));
    }

    private static ManagementException notFound(String target) {
        return new ManagementException(NOT_FOUND, errorWith(EXCEPTION_BASE_CODE + target + "NotFound"));
    }
}

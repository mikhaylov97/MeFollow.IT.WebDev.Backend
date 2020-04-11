package com.mefollow.webschool.management.user.domain;

import com.mefollow.webschool.core.exception.ApiError;
import com.mefollow.webschool.core.exception.CoreException;
import org.springframework.http.HttpStatus;

import static com.mefollow.webschool.core.exception.ApiError.errorWith;
import static org.springframework.http.HttpStatus.*;

public class AccountManagementException extends CoreException {

    private static final String EXCEPTION_BASE_CODE = "errors.backend.account.";

    public static final AccountManagementException USER_ALREADY_EXIST = badRequest("userAlreadyExist");
    public static final AccountManagementException PASSWORD_DOES_NOT_MATCH = badRequest("passwordsDontMatch", "repeatPassword");
    public static final AccountManagementException OLD_PASSWORD_DOES_NOT_MATCH = badRequest("oldPasswordDoesNotMatch", "oldPassword");

    public static final AccountManagementException UPDATE_FAILED = conflict("updateFail");
    public static final AccountManagementException EMAIL_CANNOT_BE_EMPTY = conflict("emailCannotBeNull");
    public static final AccountManagementException GENERATE_PASSWORD_FAILED = conflict("generatePasswordFail");

    public static final AccountManagementException UNAUTHORIZED = unauthorized("unauthorized");
    public static final AccountManagementException ACCOUNT_IS_BANNED = unauthorized("accountIsBanned");

    public static final AccountManagementException GONE = gone("invalidVerificationToken");
    public static final AccountManagementException USER_NOT_FOUND = notFound("email");


    private AccountManagementException(HttpStatus status, ApiError... error) {
        super(status, error);
    }

    public static AccountManagementException gone(String target) {
        return new AccountManagementException(HttpStatus.GONE, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static AccountManagementException unauthorized(String target) {
        return new AccountManagementException(HttpStatus.UNAUTHORIZED, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static AccountManagementException badRequest(String target) {
        return new AccountManagementException(BAD_REQUEST, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static AccountManagementException badRequest(String target, String fieldName) {
        return new AccountManagementException(BAD_REQUEST, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    public static AccountManagementException conflict(String target) {
        return new AccountManagementException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target));
    }

    public static AccountManagementException conflict(String target, String fieldName) {
        return new AccountManagementException(CONFLICT, errorWith(EXCEPTION_BASE_CODE + target, fieldName));
    }

    private static AccountManagementException notFound(String target) {
        return new AccountManagementException(NOT_FOUND, errorWith(EXCEPTION_BASE_CODE + target + "NotFound"));
    }
}

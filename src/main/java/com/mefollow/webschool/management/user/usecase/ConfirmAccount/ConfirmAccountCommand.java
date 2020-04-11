package com.mefollow.webschool.management.user.usecase.ConfirmAccount;

import com.mefollow.webschool.core.domain.DomainCommand;

import javax.validation.constraints.NotBlank;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class ConfirmAccountCommand extends DomainCommand {

    @NotBlank(message = fieldIsRequired)
    private String verificationToken;

    public String getVerificationToken() {
        return verificationToken;
    }

    public ConfirmAccountCommand setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        return this;
    }
}

package com.mefollow.webschool.management.user.usecase.LoginViaForm;

import javax.validation.constraints.NotEmpty;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class LoginViaFormCommand {

    @NotEmpty(message = fieldIsRequired)
    private String email;
    @NotEmpty(message = fieldIsRequired)
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginViaFormCommand setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginViaFormCommand setPassword(String password) {
        this.password = password;
        return this;
    }
}

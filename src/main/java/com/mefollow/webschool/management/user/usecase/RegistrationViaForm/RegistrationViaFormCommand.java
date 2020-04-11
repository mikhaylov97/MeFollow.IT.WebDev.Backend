package com.mefollow.webschool.management.user.usecase.RegistrationViaForm;

import com.mefollow.webschool.core.domain.DomainCommand;
import com.mefollow.webschool.management.user.domain.account.Language;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.*;

public class RegistrationViaFormCommand extends DomainCommand {

    @Email(message = incorrectEmailAddress)
    @NotBlank(message = fieldIsRequired)
    private String email;

    @Size(max = 50, message = fieldMaxLength)
    @NotBlank(message = fieldIsRequired)
    private String name;

    //TODO: implement Password annotation
    @NotBlank(message = fieldIsRequired)
    private String password;

    private Language language;

    public String getEmail() {
        return email;
    }

    public RegistrationViaFormCommand setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public RegistrationViaFormCommand setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegistrationViaFormCommand setPassword(String password) {
        this.password = password;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public RegistrationViaFormCommand setLanguage(Language language) {
        this.language = language;
        return this;
    }
}

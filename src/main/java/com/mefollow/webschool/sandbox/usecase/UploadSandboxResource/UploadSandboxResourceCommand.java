package com.mefollow.webschool.sandbox.usecase.UploadSandboxResource;

import com.mefollow.webschool.core.domain.DomainCommand;
import com.mefollow.webschool.sandbox.domain.SandboxResourceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class UploadSandboxResourceCommand extends DomainCommand {
    @NotBlank(message = fieldIsRequired)
    private String content;
    @NotNull(message = fieldIsRequired)
    private SandboxResourceType type;

    public String getContent() {
        return content;
    }

    public UploadSandboxResourceCommand setContent(String content) {
        this.content = content;
        return this;
    }

    public SandboxResourceType getType() {
        return type;
    }

    public UploadSandboxResourceCommand setType(SandboxResourceType type) {
        this.type = type;
        return this;
    }
}

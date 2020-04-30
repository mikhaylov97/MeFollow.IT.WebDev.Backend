package com.mefollow.webschool.sandbox.usecase.UpdateSandboxResource;

import com.mefollow.webschool.core.domain.DomainCommand;
import com.mefollow.webschool.sandbox.domain.base.SandboxResourceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class UpdateSandboxResourceCommand extends DomainCommand {
    private String bundleId;
    @NotBlank(message = fieldIsRequired)
    private String content;
    @NotNull(message = fieldIsRequired)
    private SandboxResourceType type;

    public String getBundleId() {
        return bundleId;
    }

    public UpdateSandboxResourceCommand setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public UpdateSandboxResourceCommand setContent(String content) {
        this.content = content;
        return this;
    }

    public SandboxResourceType getType() {
        return type;
    }

    public UpdateSandboxResourceCommand setType(SandboxResourceType type) {
        this.type = type;
        return this;
    }
}

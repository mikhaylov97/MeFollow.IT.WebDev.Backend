package com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource;

import com.mefollow.webschool.core.domain.DomainCommand;

public class DeleteSandboxResourceCommand extends DomainCommand {
    private String sandboxResourceId;

    public String getSandboxResourceId() {
        return sandboxResourceId;
    }

    public DeleteSandboxResourceCommand setSandboxResourceId(String sandboxResourceId) {
        this.sandboxResourceId = sandboxResourceId;
        return this;
    }
}

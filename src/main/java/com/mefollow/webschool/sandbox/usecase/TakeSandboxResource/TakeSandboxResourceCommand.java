package com.mefollow.webschool.sandbox.usecase.TakeSandboxResource;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeSandboxResourceCommand extends DomainCommand {
    private String sandboxResourceId;

    public String getSandboxResourceId() {
        return sandboxResourceId;
    }

    public TakeSandboxResourceCommand setSandboxResourceId(String sandboxResourceId) {
        this.sandboxResourceId = sandboxResourceId;
        return this;
    }
}

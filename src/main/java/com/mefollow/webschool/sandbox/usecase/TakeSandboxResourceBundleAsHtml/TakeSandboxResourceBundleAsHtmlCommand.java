package com.mefollow.webschool.sandbox.usecase.TakeSandboxResourceBundleAsHtml;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeSandboxResourceBundleAsHtmlCommand extends DomainCommand {
    private String bundleId;

    public String getBundleId() {
        return bundleId;
    }

    public TakeSandboxResourceBundleAsHtmlCommand setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }
}

package com.mefollow.webschool.sandbox.usecase.ResetChapterProgress;

import com.mefollow.webschool.core.domain.DomainCommand;

public class ResetChapterProgressCommand extends DomainCommand {
    private String bundleId;

    public String getBundleId() {
        return bundleId;
    }

    public ResetChapterProgressCommand setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }
}

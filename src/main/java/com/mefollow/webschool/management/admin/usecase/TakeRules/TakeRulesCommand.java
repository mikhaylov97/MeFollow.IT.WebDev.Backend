package com.mefollow.webschool.management.admin.usecase.TakeRules;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeRulesCommand extends DomainCommand {
    private String chapterId;

    public String getChapterId() {
        return chapterId;
    }

    public TakeRulesCommand setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }
}

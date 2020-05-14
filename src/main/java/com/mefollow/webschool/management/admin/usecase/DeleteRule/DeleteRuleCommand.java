package com.mefollow.webschool.management.admin.usecase.DeleteRule;

import com.mefollow.webschool.core.domain.DomainCommand;

public class DeleteRuleCommand extends DomainCommand {
    private String chapterId;
    private String ruleId;

    public String getChapterId() {
        return chapterId;
    }

    public DeleteRuleCommand setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public String getRuleId() {
        return ruleId;
    }

    public DeleteRuleCommand setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return this;
    }
}

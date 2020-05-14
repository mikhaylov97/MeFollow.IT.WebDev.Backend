package com.mefollow.webschool.checker.usecase.CheckAnswer;

import com.mefollow.webschool.core.domain.DomainCommand;

public class CheckAnswerCommand extends DomainCommand {
    private String chapterId;

    public String getChapterId() {
        return chapterId;
    }

    public CheckAnswerCommand setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }
}

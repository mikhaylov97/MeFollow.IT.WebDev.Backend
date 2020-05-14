package com.mefollow.webschool.management.admin.usecase.TakeLessons;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeLessonsCommand extends DomainCommand {
    private String topicId;

    public String getTopicId() {
        return topicId;
    }

    public TakeLessonsCommand setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }
}

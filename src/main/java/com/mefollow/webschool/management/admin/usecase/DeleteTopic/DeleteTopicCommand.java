package com.mefollow.webschool.management.admin.usecase.DeleteTopic;

import com.mefollow.webschool.core.domain.DomainCommand;

public class DeleteTopicCommand extends DomainCommand {
    private String courseId;
    private String topicId;

    public String getCourseId() {
        return courseId;
    }

    public DeleteTopicCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getTopicId() {
        return topicId;
    }

    public DeleteTopicCommand setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }
}

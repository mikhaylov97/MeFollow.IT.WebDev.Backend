package com.mefollow.webschool.management.admin.usecase.DeleteLesson;

import com.mefollow.webschool.core.domain.DomainCommand;

public class DeleteLessonCommand extends DomainCommand {
    private String topicId;
    private String lessonId;

    public String getTopicId() {
        return topicId;
    }

    public DeleteLessonCommand setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }

    public String getLessonId() {
        return lessonId;
    }

    public DeleteLessonCommand setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }
}

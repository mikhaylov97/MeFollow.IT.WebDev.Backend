package com.mefollow.webschool.management.admin.usecase.TakeTopics;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeTopicsCommand extends DomainCommand {
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public TakeTopicsCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }
}

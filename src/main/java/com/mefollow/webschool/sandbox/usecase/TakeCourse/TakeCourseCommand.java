package com.mefollow.webschool.sandbox.usecase.TakeCourse;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeCourseCommand extends DomainCommand {
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public TakeCourseCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }
}

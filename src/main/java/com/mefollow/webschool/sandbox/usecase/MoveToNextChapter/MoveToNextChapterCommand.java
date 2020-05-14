package com.mefollow.webschool.sandbox.usecase.MoveToNextChapter;

import com.mefollow.webschool.core.domain.DomainCommand;

public class MoveToNextChapterCommand extends DomainCommand {
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public MoveToNextChapterCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }
}

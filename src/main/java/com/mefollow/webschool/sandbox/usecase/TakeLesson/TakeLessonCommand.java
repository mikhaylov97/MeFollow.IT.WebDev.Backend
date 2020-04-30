package com.mefollow.webschool.sandbox.usecase.TakeLesson;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeLessonCommand extends DomainCommand {
    private String courseId;
    private String lessonId;

    public String getCourseId() {
        return courseId;
    }

    public TakeLessonCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getLessonId() {
        return lessonId;
    }

    public TakeLessonCommand setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }
}

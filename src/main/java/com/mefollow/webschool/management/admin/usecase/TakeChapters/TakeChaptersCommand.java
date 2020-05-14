package com.mefollow.webschool.management.admin.usecase.TakeChapters;

import com.mefollow.webschool.core.domain.DomainCommand;

public class TakeChaptersCommand extends DomainCommand {
    private String lessonId;

    public String getLessonId() {
        return lessonId;
    }

    public TakeChaptersCommand setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }
}

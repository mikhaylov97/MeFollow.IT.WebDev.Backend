package com.mefollow.webschool.management.admin.usecase.DeleteChapter;

import com.mefollow.webschool.core.domain.DomainCommand;

public class DeleteChapterCommand extends DomainCommand {
    private String lessonId;
    private String chapterId;

    public String getLessonId() {
        return lessonId;
    }

    public DeleteChapterCommand setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }

    public String getChapterId() {
        return chapterId;
    }

    public DeleteChapterCommand setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }
}

package com.mefollow.webschool.sandbox.domain.dto;

import com.mefollow.webschool.sandbox.domain.base.Course;

public class FullCourseDto {
    private String id;
    private String title;
    private String description;
    private boolean isLearningStarted;
    private String continueUrl;

    public FullCourseDto(Course course, String continueUrl, boolean isLearningStarted) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getFullDescription();
        this.isLearningStarted = isLearningStarted;
        this.continueUrl = continueUrl;
    }

    public String getId() {
        return id;
    }

    public FullCourseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public FullCourseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FullCourseDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isLearningStarted() {
        return isLearningStarted;
    }

    public FullCourseDto setLearningStarted(boolean learningStarted) {
        isLearningStarted = learningStarted;
        return this;
    }

    public String getContinueUrl() {
        return continueUrl;
    }

    public FullCourseDto setContinueUrl(String continueUrl) {
        this.continueUrl = continueUrl;
        return this;
    }
}

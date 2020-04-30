package com.mefollow.webschool.sandbox.domain.dto;

import com.mefollow.webschool.sandbox.domain.base.Course;

public class BriefCourseDto {
    private String id;
    private String title;
    private String description;

    public BriefCourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getBriefDescription();
    }

    public String getId() {
        return id;
    }

    public BriefCourseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BriefCourseDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BriefCourseDto setDescription(String description) {
        this.description = description;
        return this;
    }
}

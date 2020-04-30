package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-lessons")
public class Lesson extends BaseModel {
    private String courseId;
    private String title;
    private String description;
    private int orderIndex;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
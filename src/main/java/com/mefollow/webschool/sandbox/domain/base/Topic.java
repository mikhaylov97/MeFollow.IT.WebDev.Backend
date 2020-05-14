package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-topics")
public class Topic extends BaseModel {
    private String courseId;
    private String title;
    private int orderIndex;

    public Topic(String courseId, String title, int orderIndex) {
        this.courseId = courseId;
        this.title = title;
        this.orderIndex = orderIndex;
    }

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

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}

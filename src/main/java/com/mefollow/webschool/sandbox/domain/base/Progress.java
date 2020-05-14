package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-progresses")
public class Progress extends BaseModel {
    private String userId;
    private String chapterId;
    private String courseId;
    private boolean solved;

    public Progress(String userId, String chapterId, String courseId) {
        this.userId = userId;
        this.chapterId = chapterId;
        this.courseId = courseId;
        this.solved = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}

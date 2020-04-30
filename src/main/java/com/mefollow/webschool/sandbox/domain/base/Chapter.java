package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "sandbox-chapters")
public class Chapter extends BaseModel {
    private String lessonId;
    private String description;
    private int orderIndex;

    private Map<SandboxResourceType, String> initialState;

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
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

    public Map<SandboxResourceType, String> getInitialState() {
        return initialState;
    }

    public boolean isHtmlEnabled() {
        return initialState.containsKey(SandboxResourceType.HTML);
    }

    public boolean isCssEnabled() {
        return initialState.containsKey(SandboxResourceType.CSS);
    }

    public boolean isJsEnabled() {
        return initialState.containsKey(SandboxResourceType.JS);
    }

    public void setInitialState(Map<SandboxResourceType, String> initialState) {
        this.initialState = initialState;
    }
}

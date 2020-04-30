package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-resource-bundles")
public class SandboxResourceBundle extends BaseModel {
    private String userId;
    private String chapterId;

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
}

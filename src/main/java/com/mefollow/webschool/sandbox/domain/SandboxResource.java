package com.mefollow.webschool.sandbox.domain;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-resources")
public class SandboxResource extends BaseModel {
    private String userId;
    private String content;
    private SandboxResourceType type;

    public SandboxResource(String userId, String content, SandboxResourceType type) {
        this.userId = userId;
        this.content = content;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SandboxResourceType getType() {
        return type;
    }

    public void setType(SandboxResourceType type) {
        this.type = type;
    }
}

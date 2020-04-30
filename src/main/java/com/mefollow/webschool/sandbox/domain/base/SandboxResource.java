package com.mefollow.webschool.sandbox.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-resources")
public class SandboxResource extends BaseModel {
    private String bundleId;
    private String content;
    private SandboxResourceType type;

    public SandboxResource(String bundleId, String content, SandboxResourceType type) {
        this.bundleId = bundleId;
        this.content = content;
        this.type = type;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
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

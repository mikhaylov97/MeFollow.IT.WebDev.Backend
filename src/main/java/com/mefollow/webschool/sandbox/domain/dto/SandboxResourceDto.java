package com.mefollow.webschool.sandbox.domain.dto;

public class SandboxResourceDto {
    private String resourceId;
    private String content;

    public String getResourceId() {
        return resourceId;
    }

    public SandboxResourceDto setResourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SandboxResourceDto setContent(String content) {
        this.content = content;
        return this;
    }
}

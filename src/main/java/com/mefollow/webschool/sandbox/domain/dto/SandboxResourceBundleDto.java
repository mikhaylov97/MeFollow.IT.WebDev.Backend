package com.mefollow.webschool.sandbox.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SandboxResourceBundleDto {
    private String bundleId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String htmlContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cssContent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jsContent;

    public String getBundleId() {
        return bundleId;
    }

    public SandboxResourceBundleDto setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public SandboxResourceBundleDto setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
        return this;
    }

    public String getCssContent() {
        return cssContent;
    }

    public SandboxResourceBundleDto setCssContent(String cssContent) {
        this.cssContent = cssContent;
        return this;
    }

    public String getJsContent() {
        return jsContent;
    }

    public SandboxResourceBundleDto setJsContent(String jsContent) {
        this.jsContent = jsContent;
        return this;
    }
}

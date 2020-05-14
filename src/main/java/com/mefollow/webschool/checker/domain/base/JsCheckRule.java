package com.mefollow.webschool.checker.domain.base;

public class JsCheckRule extends CheckRule {
    private String content;
    private boolean executable;

    public JsCheckRule(String chapterId, String commonErrorMessage, String content, boolean executable) {
        super(CheckRuleType.JS);
        this.chapterId = chapterId;
        this.commonErrorMessage = commonErrorMessage;
        this.content = content;
        this.executable = executable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }
}

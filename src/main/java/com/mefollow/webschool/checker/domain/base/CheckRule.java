package com.mefollow.webschool.checker.domain.base;

import com.mefollow.webschool.core.domain.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sandbox-check-rules")
public abstract class CheckRule extends BaseModel {
    protected String chapterId;
    protected String commonErrorMessage;
    protected CheckRuleType type;

    protected CheckRule(CheckRuleType type) {
        this.type = type;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getCommonErrorMessage() {
        return commonErrorMessage;
    }

    public void setCommonErrorMessage(String commonErrorMessage) {
        this.commonErrorMessage = commonErrorMessage;
    }

    public CheckRuleType getType() {
        return type;
    }

    public void setType(CheckRuleType type) {
        this.type = type;
    }
}

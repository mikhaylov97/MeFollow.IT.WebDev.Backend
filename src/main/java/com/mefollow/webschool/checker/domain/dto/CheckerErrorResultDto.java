package com.mefollow.webschool.checker.domain.dto;

import com.mefollow.webschool.checker.domain.base.CheckRuleType;

public class CheckerErrorResultDto extends CheckerResultDto {
    private CheckRuleType errorSource;

    public CheckerErrorResultDto(String errorMessage, CheckRuleType errorSource) {
        super(false, errorMessage);
        this.errorSource = errorSource;
    }

    public CheckRuleType getErrorSource() {
        return errorSource;
    }

    public CheckerErrorResultDto setErrorSource(CheckRuleType errorSource) {
        this.errorSource = errorSource;
        return this;
    }
}

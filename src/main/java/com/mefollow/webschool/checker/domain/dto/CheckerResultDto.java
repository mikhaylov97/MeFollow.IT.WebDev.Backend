package com.mefollow.webschool.checker.domain.dto;

public class CheckerResultDto {
    private boolean successful;
    private String resultMessage;

    public CheckerResultDto() {
        this.successful = true;
    }

    public CheckerResultDto(String resultMessage) {
        this.successful = true;
        this.resultMessage = resultMessage;
    }

    protected CheckerResultDto(boolean successful, String errorMessage) {
        this.successful = successful;
        this.resultMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public CheckerResultDto setSuccessful(boolean successful) {
        this.successful = successful;
        return this;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public CheckerResultDto setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }
}

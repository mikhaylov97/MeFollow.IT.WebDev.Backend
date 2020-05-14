package com.mefollow.webschool.checker.domain.base;

import java.util.Map;

public class CssCheckRule extends CheckRule {
    private String htmlCheckRuleId;
    private Map<String, CssProperty> cssProperties;

    public CssCheckRule(String chapterId, String commonErrorMessage, String htmlCheckRuleId) {
        super(CheckRuleType.CSS);
        this.chapterId = chapterId;
        this.commonErrorMessage = commonErrorMessage;
        this.htmlCheckRuleId = htmlCheckRuleId;
    }

    public String getHtmlCheckRuleId() {
        return htmlCheckRuleId;
    }

    public void setHtmlCheckRuleId(String htmlCheckRuleId) {
        this.htmlCheckRuleId = htmlCheckRuleId;
    }

    public Map<String, CssProperty> getCssProperties() {
        return cssProperties;
    }

    public void setCssProperties(Map<String, CssProperty> cssProperties) {
        this.cssProperties = cssProperties;
    }

    public static class CssProperty {
        private String propertyValue;
        private String errorMessage;

        public CssProperty(String propertyValue, String errorMessage) {
            this.propertyValue = propertyValue;
            this.errorMessage = errorMessage;
        }

        public String getPropertyValue() {
            return propertyValue;
        }

        public void setPropertyValue(String propertyValue) {
            this.propertyValue = propertyValue;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

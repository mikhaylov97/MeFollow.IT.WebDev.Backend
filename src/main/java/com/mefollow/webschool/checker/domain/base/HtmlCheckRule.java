package com.mefollow.webschool.checker.domain.base;

import java.util.Map;
import java.util.Set;

public class HtmlCheckRule extends CheckRule {

    private String cssSelector;
    private HtmlTag tagName;

    private String tagId;
    private String tagIdErrorMessage;

    private Set<HtmlClass> tagClasses;

    private Map<String, HtmlAttribute> tagAttributes;

    private String tagInnerContent;
    private String tagInnerContentErrorMessage;

    public HtmlCheckRule(String chapterId, String commonErrorMessage, String cssSelector, HtmlTag tagName) {
        super(CheckRuleType.HTML);
        this.chapterId = chapterId;
        this.commonErrorMessage = commonErrorMessage;
        this.cssSelector = cssSelector;
        this.tagName = tagName;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public boolean hasTagId() {
        return this.tagId != null;
    }

    public String getTagIdErrorMessage() {
        return tagIdErrorMessage;
    }

    public void setTagIdErrorMessage(String tagIdErrorMessage) {
        this.tagIdErrorMessage = tagIdErrorMessage;
    }

    public HtmlTag getTagName() {
        return tagName;
    }

    public void setTagName(HtmlTag tagName) {
        this.tagName = tagName;
    }

    public Set<HtmlClass> getTagClasses() {
        return tagClasses;
    }

    public void setTagClasses(Set<HtmlClass> tagClasses) {
        this.tagClasses = tagClasses;
    }

    public boolean hasTagClasses() {
        return this.tagClasses != null;
    }

    public Map<String, HtmlAttribute> getTagAttributes() {
        return tagAttributes;
    }

    public void setTagAttributes(Map<String, HtmlAttribute> tagAttributes) {
        this.tagAttributes = tagAttributes;
    }

    public boolean hasTagAttributes() {
        return this.tagAttributes != null;
    }

    public String getTagInnerContent() {
        return tagInnerContent;
    }

    public void setTagInnerContent(String tagInnerContent) {
        this.tagInnerContent = tagInnerContent;
    }

    public boolean hasTagInnerContent() {
        return this.tagInnerContent != null;
    }

    public String getTagInnerContentErrorMessage() {
        return tagInnerContentErrorMessage;
    }

    public void setTagInnerContentErrorMessage(String tagInnerContentErrorMessage) {
        this.tagInnerContentErrorMessage = tagInnerContentErrorMessage;
    }

    public static class HtmlClass {
        private String className;
        private String errorMessage;

        public HtmlClass(String className, String errorMessage) {
            this.className = className;
            this.errorMessage = errorMessage;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public static class HtmlAttribute {
        private String attributeValue;
        private String errorMessage;

        public HtmlAttribute(String attributeValue, String errorMessage) {
            this.attributeValue = attributeValue;
            this.errorMessage = errorMessage;
        }

        public String getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

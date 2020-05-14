package com.mefollow.webschool.management.admin.usecase.CreateRule;

import com.mefollow.webschool.checker.domain.base.CheckRuleType;
import com.mefollow.webschool.checker.domain.base.CssCheckRule;
import com.mefollow.webschool.checker.domain.base.HtmlCheckRule;
import com.mefollow.webschool.core.domain.DomainCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class CreateRuleCommand extends DomainCommand {
    private String chapterId;

    @NotBlank(message = fieldIsRequired)
    private String commonErrorMessage;

    @NotNull(message = fieldIsRequired)
    private CheckRuleType type;

    //HtmlCheckRule
    private String cssSelector;
    private String tagName;

    private String tagId;
    private String tagIdErrorMessage;

    private Set<HtmlCheckRule.HtmlClass> tagClasses;

    private Map<String, HtmlCheckRule.HtmlAttribute> tagAttributes;

    private String tagInnerContent;
    private String tagInnerContentErrorMessage;

    //CssCheckRule
    private String htmlCheckRuleId;
    private Map<String, CssCheckRule.CssProperty> cssProperties;

    //JsCheckRule
    private String content;
    private Boolean executable;

    public String getChapterId() {
        return chapterId;
    }

    public CreateRuleCommand setChapterId(String chapterId) {
        this.chapterId = chapterId;
        return this;
    }

    public String getCommonErrorMessage() {
        return commonErrorMessage;
    }

    public CreateRuleCommand setCommonErrorMessage(String commonErrorMessage) {
        this.commonErrorMessage = commonErrorMessage;
        return this;
    }

    public CheckRuleType getType() {
        return type;
    }

    public CreateRuleCommand setType(CheckRuleType type) {
        this.type = type;
        return this;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public CreateRuleCommand setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
        return this;
    }

    public String getTagName() {
        return tagName;
    }

    public CreateRuleCommand setTagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getTagId() {
        return tagId;
    }

    public CreateRuleCommand setTagId(String tagId) {
        this.tagId = tagId;
        return this;
    }

    public String getTagIdErrorMessage() {
        return tagIdErrorMessage;
    }

    public CreateRuleCommand setTagIdErrorMessage(String tagIdErrorMessage) {
        this.tagIdErrorMessage = tagIdErrorMessage;
        return this;
    }

    public Set<HtmlCheckRule.HtmlClass> getTagClasses() {
        return tagClasses;
    }

    public CreateRuleCommand setTagClasses(Set<HtmlCheckRule.HtmlClass> tagClasses) {
        this.tagClasses = tagClasses;
        return this;
    }

    public Map<String, HtmlCheckRule.HtmlAttribute> getTagAttributes() {
        return tagAttributes;
    }

    public CreateRuleCommand setTagAttributes(Map<String, HtmlCheckRule.HtmlAttribute> tagAttributes) {
        this.tagAttributes = tagAttributes;
        return this;
    }

    public String getTagInnerContent() {
        return tagInnerContent;
    }

    public CreateRuleCommand setTagInnerContent(String tagInnerContent) {
        this.tagInnerContent = tagInnerContent;
        return this;
    }

    public String getTagInnerContentErrorMessage() {
        return tagInnerContentErrorMessage;
    }

    public CreateRuleCommand setTagInnerContentErrorMessage(String tagInnerContentErrorMessage) {
        this.tagInnerContentErrorMessage = tagInnerContentErrorMessage;
        return this;
    }

    public String getHtmlCheckRuleId() {
        return htmlCheckRuleId;
    }

    public CreateRuleCommand setHtmlCheckRuleId(String htmlCheckRuleId) {
        this.htmlCheckRuleId = htmlCheckRuleId;
        return this;
    }

    public Map<String, CssCheckRule.CssProperty> getCssProperties() {
        return cssProperties;
    }

    public CreateRuleCommand setCssProperties(Map<String, CssCheckRule.CssProperty> cssProperties) {
        this.cssProperties = cssProperties;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CreateRuleCommand setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getExecutable() {
        return executable;
    }

    public CreateRuleCommand setExecutable(Boolean executable) {
        this.executable = executable;
        return this;
    }
}

package com.mefollow.webschool.management.admin.usecase.CreateChapter;

import com.mefollow.webschool.core.domain.DomainCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class CreateChapterCommand extends DomainCommand {
    private String lessonId;

    @NotBlank(message = fieldIsRequired)
    private String description;

    @NotNull(message = fieldIsRequired)
    private Integer orderIndex;

    @NotBlank(message = fieldIsRequired)
    private String htmlContent;

    private String cssContent;
    private String jsContent;

    public String getLessonId() {
        return lessonId;
    }

    public CreateChapterCommand setLessonId(String lessonId) {
        this.lessonId = lessonId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateChapterCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public CreateChapterCommand setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public CreateChapterCommand setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
        return this;
    }

    public String getCssContent() {
        return cssContent;
    }

    public CreateChapterCommand setCssContent(String cssContent) {
        this.cssContent = cssContent;
        return this;
    }

    public String getJsContent() {
        return jsContent;
    }

    public CreateChapterCommand setJsContent(String jsContent) {
        this.jsContent = jsContent;
        return this;
    }
}

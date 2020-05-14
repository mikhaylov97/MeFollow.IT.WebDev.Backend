package com.mefollow.webschool.management.admin.usecase.CreateTopic;

import com.mefollow.webschool.core.domain.DomainCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class CreateTopicCommand extends DomainCommand {
    private String courseId;

    @NotBlank(message = fieldIsRequired)
    private String title;

    @NotNull(message = fieldIsRequired)
    private Integer orderIndex;

    public String getCourseId() {
        return courseId;
    }

    public CreateTopicCommand setCourseId(String courseId) {
        this.courseId = courseId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateTopicCommand setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public CreateTopicCommand setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }
}

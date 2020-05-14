package com.mefollow.webschool.management.admin.usecase.CreateLesson;

import com.mefollow.webschool.core.domain.DomainCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.mefollow.webschool.core.exception.ValidationErrorMessages.fieldIsRequired;

public class CreateLessonCommand extends DomainCommand {
    private String topicId;

    @NotBlank(message = fieldIsRequired)
    private String title;

    @NotBlank(message = fieldIsRequired)
    private String description;

    @NotNull(message = fieldIsRequired)
    private Integer orderIndex;

    public String getTopicId() {
        return topicId;
    }

    public CreateLessonCommand setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateLessonCommand setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateLessonCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public CreateLessonCommand setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
        return this;
    }
}

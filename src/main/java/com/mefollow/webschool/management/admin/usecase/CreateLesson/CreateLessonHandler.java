package com.mefollow.webschool.management.admin.usecase.CreateLesson;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.sandbox.domain.base.Lesson;
import com.mefollow.webschool.sandbox.infrastructure.repository.LessonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.ManagementException.LESSON_ALREADY_EXISTS;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;

@Service
public class CreateLessonHandler {

    private final LessonRepository lessonRepository;
    private final PermissionService permissionService;

    public CreateLessonHandler(LessonRepository lessonRepository, PermissionService permissionService) {
        this.lessonRepository = lessonRepository;
        this.permissionService = permissionService;
    }

    public Mono<Lesson> handle(CreateLessonCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<Lesson> processHandling(CreateLessonCommand command) {
        return lessonRepository.existsLessonByTopicIdAndOrderIndex(command.getTopicId(), command.getOrderIndex())
                .flatMap(lessonExists -> !lessonExists
                        ? lessonRepository.save(new Lesson(command.getTopicId(), command.getTitle(), command.getDescription(), command.getOrderIndex()))
                        : error(LESSON_ALREADY_EXISTS));
    }
}

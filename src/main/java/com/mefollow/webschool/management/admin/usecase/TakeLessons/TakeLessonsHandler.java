package com.mefollow.webschool.management.admin.usecase.TakeLessons;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.LessonsDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.LessonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeLessonsHandler {

    private final LessonRepository lessonRepository;
    private final PermissionService permissionService;

    public TakeLessonsHandler(LessonRepository lessonRepository,
                              PermissionService permissionService) {
        this.lessonRepository = lessonRepository;
        this.permissionService = permissionService;
    }

    public Mono<LessonsDto> handle(TakeLessonsCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<LessonsDto> processHandling(TakeLessonsCommand command) {
        return lessonRepository.findAllByTopicIdOrderByOrderIndex(command.getTopicId())
                .collectList()
                .map(lessons -> {
                    final var dto = new LessonsDto();
                    dto.setMinAvailableOrderIndex(lessons.size() + 1);
                    lessons.forEach(lesson -> dto.addLesson(lesson.getId(), lesson.getTitle(), lesson.getOrderIndex()));

                    return dto;
                });
    }
}

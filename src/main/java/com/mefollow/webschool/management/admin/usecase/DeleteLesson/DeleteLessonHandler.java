package com.mefollow.webschool.management.admin.usecase.DeleteLesson;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.LessonsDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.LessonRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class DeleteLessonHandler {

    private final LessonRepository lessonRepository;
    private final PermissionService permissionService;

    public DeleteLessonHandler(LessonRepository lessonRepository, PermissionService permissionService) {
        this.lessonRepository = lessonRepository;
        this.permissionService = permissionService;
    }

    public Mono<LessonsDto> handle(DeleteLessonCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<LessonsDto> processHandling(DeleteLessonCommand command) {
        final var dto = new LessonsDto();
        return lessonRepository.findAllByTopicIdOrderByOrderIndex(command.getTopicId())
                .collectList()
                .map(lessons -> {
                    lessons.removeIf(lesson -> lesson.getId().equals(command.getLessonId()));
                    dto.setMinAvailableOrderIndex(lessons.size() + 1);

                    for (int i = 0; i < lessons.size(); i++) {
                        lessons.get(i).setOrderIndex(i + 1);
                        dto.addLesson(lessons.get(i).getId(), lessons.get(i).getTitle(), lessons.get(i).getOrderIndex());
                    }

                    return lessons;
                })
                .flatMapMany(lessonRepository::saveAll)
                .then(lessonRepository.deleteById(command.getLessonId()))
                .then(just(dto));
    }
}

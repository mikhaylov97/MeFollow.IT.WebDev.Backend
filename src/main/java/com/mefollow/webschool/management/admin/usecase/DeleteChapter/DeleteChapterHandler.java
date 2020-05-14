package com.mefollow.webschool.management.admin.usecase.DeleteChapter;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.ChaptersDto;
import com.mefollow.webschool.management.admin.usecase.DeleteLesson.DeleteLessonCommand;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class DeleteChapterHandler {

    private static final String CHAPTER_TITLE = "%s-ая глава урока";

    private final PermissionService permissionService;
    private final ChapterRepository chapterRepository;

    public DeleteChapterHandler(PermissionService permissionService,
                                ChapterRepository chapterRepository) {
        this.permissionService = permissionService;
        this.chapterRepository = chapterRepository;
    }

    public Mono<ChaptersDto> handle(DeleteChapterCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<ChaptersDto> processHandling(DeleteChapterCommand command) {
        final var dto = new ChaptersDto();
        return chapterRepository.findAllByLessonIdOrderByOrderIndex(command.getLessonId())
                .collectList()
                .map(chapters -> {
                    chapters.removeIf(chapter -> chapter.getId().equals(command.getChapterId()));
                    dto.setMinAvailableOrderIndex(chapters.size() + 1);

                    for (int i = 0; i < chapters.size(); i++) {
                        chapters.get(i).setOrderIndex(i + 1);
                        dto.addChapter(chapters.get(i).getId(), format(CHAPTER_TITLE, chapters.get(i).getOrderIndex()), chapters.get(i).getOrderIndex());
                    }

                    return chapters;
                })
                .flatMapMany(chapterRepository::saveAll)
                .then(chapterRepository.deleteById(command.getChapterId()))
                .then(just(dto));
    }
}

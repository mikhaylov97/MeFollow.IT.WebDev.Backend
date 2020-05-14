package com.mefollow.webschool.management.admin.usecase.TakeChapters;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.ChaptersDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeChaptersHandler {

    private static final String CHAPTER_TITLE = "%s-ая глава урока";

    private final PermissionService permissionService;
    private final ChapterRepository chapterRepository;

    public TakeChaptersHandler(PermissionService permissionService,
                               ChapterRepository chapterRepository) {
        this.permissionService = permissionService;
        this.chapterRepository = chapterRepository;
    }

    public Mono<ChaptersDto> handle(TakeChaptersCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<ChaptersDto> processHandling(TakeChaptersCommand command) {
        return chapterRepository.findAllByLessonIdOrderByOrderIndex(command.getLessonId())
                .collectList()
                .map(chapters -> {
                    final var dto = new ChaptersDto();
                    dto.setMinAvailableOrderIndex(chapters.size() + 1);
                    chapters.forEach(chapter -> dto.addChapter(chapter.getId(), format(CHAPTER_TITLE, chapter.getOrderIndex()), chapter.getOrderIndex()));

                    return dto;
                });
    }
}

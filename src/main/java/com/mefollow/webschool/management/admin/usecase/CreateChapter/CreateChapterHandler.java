package com.mefollow.webschool.management.admin.usecase.CreateChapter;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.sandbox.domain.base.Chapter;
import com.mefollow.webschool.sandbox.domain.base.SandboxResourceType;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.ManagementException.CHAPTER_ALREADY_EXISTS;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static com.mefollow.webschool.sandbox.domain.base.SandboxResourceType.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static reactor.core.publisher.Mono.error;

@Service
public class CreateChapterHandler {

    private final PermissionService permissionService;
    private final ChapterRepository chapterRepository;

    public CreateChapterHandler(PermissionService permissionService, ChapterRepository chapterRepository) {
        this.permissionService = permissionService;
        this.chapterRepository = chapterRepository;
    }

    public Mono<Chapter> handle(CreateChapterCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<Chapter> processHandling(CreateChapterCommand command) {
        return chapterRepository.existsChapterByLessonIdAndOrderIndex(command.getLessonId(), command.getOrderIndex())
                .flatMap(exists -> !exists
                        ? chapterRepository.save(createChapter(command))
                        : error(CHAPTER_ALREADY_EXISTS));
    }

    private Chapter createChapter(CreateChapterCommand command) {
        final var chapter = new Chapter();
        chapter.setLessonId(command.getLessonId());
        chapter.setDescription(command.getDescription());
        chapter.setOrderIndex(command.getOrderIndex());

        final var initialState = new HashMap<SandboxResourceType, String>();
        initialState.put(HTML, command.getHtmlContent());
        if (isNotBlank(command.getCssContent())) initialState.put(CSS, command.getCssContent());
        if (isNotBlank(command.getJsContent())) initialState.put(JS, command.getJsContent());
        chapter.setInitialState(initialState);

        return chapter;
    }
}

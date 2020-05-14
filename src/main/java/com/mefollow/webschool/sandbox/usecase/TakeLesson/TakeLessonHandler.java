package com.mefollow.webschool.sandbox.usecase.TakeLesson;

import com.mefollow.webschool.sandbox.domain.base.Chapter;
import com.mefollow.webschool.sandbox.domain.base.Progress;
import com.mefollow.webschool.sandbox.domain.base.SandboxResource;
import com.mefollow.webschool.sandbox.domain.base.SandboxResourceBundle;
import com.mefollow.webschool.sandbox.domain.dto.LessonDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class TakeLessonHandler {

    private static final String BUNDLE_URL_TEMPLATE = "http://localhost:8080/sandbox/bundle/%s";

    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final ProgressRepository progressRepository;
    private final SandboxResourceRepository sandboxResourceRepository;
    private final SandboxResourceBundleRepository sandboxResourceBundleRepository;

    public TakeLessonHandler(LessonRepository lessonRepository,
                             ChapterRepository chapterRepository,
                             ProgressRepository progressRepository,
                             SandboxResourceRepository sandboxResourceRepository,
                             SandboxResourceBundleRepository sandboxResourceBundleRepository) {
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.progressRepository = progressRepository;
        this.sandboxResourceRepository = sandboxResourceRepository;
        this.sandboxResourceBundleRepository = sandboxResourceBundleRepository;
    }

    @SuppressWarnings("Duplicates")
    public Mono<LessonDto> handle(TakeLessonCommand command) {
        return lessonRepository.findById(command.getLessonId())
                .switchIfEmpty(error(LESSON_NOT_FOUND))
                .flatMap(lesson -> progressRepository.findFirstByUserIdAndCourseId(command.getUserId(), command.getCourseId())
                        .switchIfEmpty(createDefaultProgress(command.getUserId(), command.getCourseId(), lesson.getId()))
                        .flatMap(progress -> chapterRepository.findById(progress.getChapterId())
                                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                                .zipWith(chapterRepository.countAllByLessonId(lesson.getId()))
                                .flatMap(chapter -> sandboxResourceBundleRepository.findByChapterIdAndUserId(chapter.getT1().getId(), command.getUserId())
                                        .switchIfEmpty(createDefaultSandboxResourceBundle(command.getUserId(), chapter.getT1()))
                                        .flatMap(bundle -> sandboxResourceRepository.findAllByBundleId(bundle.getId())
                                                .collectList()
                                                .flatMap(resources -> {
                                                    if (resources.isEmpty()) return error(SANDBOX_RESOURCES_INCONSISTENCY);

                                                    final var bundleUrl = format(BUNDLE_URL_TEMPLATE, bundle.getId());
                                                    final var displayTheory = chapter.getT1().getOrderIndex() == 1;
                                                    final var lessonDto = new LessonDto(lesson, chapter.getT1(), chapter.getT2().intValue(), progress.isSolved(), displayTheory, bundle.getId(), bundleUrl);
                                                    resources.forEach(resource -> {
                                                        switch (resource.getType()) {
                                                            case HTML:
                                                                lessonDto.setHtmlContent(resource.getContent());
                                                                break;
                                                            case CSS:
                                                                lessonDto.setCssContent(resource.getContent());
                                                                break;
                                                            case JS:
                                                                lessonDto.setJsContent(resource.getContent());
                                                                break;
                                                        }
                                                    });

                                                    return just(lessonDto);
                                                })))));
    }

    private Mono<Progress> createDefaultProgress(String userId, String courseId, String lessonId) {
        return chapterRepository.findFirstByLessonIdOrderByOrderIndex(lessonId)
                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                .map(firstChapter -> new Progress(userId, firstChapter.getId(), courseId))
                .flatMap(progressRepository::save);
    }

    private Mono<SandboxResourceBundle> createDefaultSandboxResourceBundle(String userId, Chapter chapter) {
        final var bundle = new SandboxResourceBundle();
        bundle.setUserId(userId);
        bundle.setChapterId(chapter.getId());

        return sandboxResourceBundleRepository.save(bundle)
                .flatMap(savedBundle -> {
                    final var resources = new ArrayList<SandboxResource>();
                    chapter.getInitialState().forEach((type, content) -> resources.add(new SandboxResource(savedBundle.getId(), content, type)));
                    return sandboxResourceRepository.saveAll(resources).then(just(savedBundle));
                });
    }
}

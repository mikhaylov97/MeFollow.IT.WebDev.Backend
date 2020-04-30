package com.mefollow.webschool.sandbox.usecase.ResetChapterProgress;

import com.mefollow.webschool.sandbox.domain.dto.SandboxResourceBundleDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceBundleRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class ResetChapterProgressHandler {

    private final ChapterRepository chapterRepository;
    private final SandboxResourceRepository sandboxResourceRepository;
    private final SandboxResourceBundleRepository sandboxResourceBundleRepository;

    public ResetChapterProgressHandler(ChapterRepository chapterRepository,
                                       SandboxResourceRepository sandboxResourceRepository,
                                       SandboxResourceBundleRepository sandboxResourceBundleRepository) {
        this.chapterRepository = chapterRepository;
        this.sandboxResourceRepository = sandboxResourceRepository;
        this.sandboxResourceBundleRepository = sandboxResourceBundleRepository;
    }

    @SuppressWarnings("Duplicates")
    public Mono<SandboxResourceBundleDto> handle(ResetChapterProgressCommand command) {
        return sandboxResourceBundleRepository.findById(command.getBundleId())
                .switchIfEmpty(error(SANDBOX_RESOURCE_BUNDLE_NOT_FOUND))
                .flatMap(bundle -> {
                    if (!bundle.getUserId().equals(command.getUserId())) return error(SANDBOX_RESOURCE_BUNDLE_FORBIDDEN);
                    return just(bundle);
                })
                .flatMap(bundle -> chapterRepository.findById(bundle.getChapterId())
                        .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                        .flatMap(chapter -> sandboxResourceRepository.findAllByBundleId(bundle.getId())
                                .map(resource -> {
                                    ofNullable(chapter.getInitialState().get(resource.getType())).ifPresent(resource::setContent);
                                    return resource;
                                })
                                .flatMap(sandboxResourceRepository::save)
                                .collectList()
                                .map(resources -> {
                                    final var bundleDto = new SandboxResourceBundleDto();
                                    bundleDto.setBundleId(bundle.getId());
                                    resources.forEach(resource -> {
                                        switch (resource.getType()) {
                                            case HTML:
                                                bundleDto.setHtmlContent(resource.getContent());
                                                break;
                                            case CSS:
                                                bundleDto.setCssContent(resource.getContent());
                                                break;
                                            case JS:
                                                bundleDto.setJsContent(resource.getContent());
                                                break;
                                        }
                                    });

                                    return bundleDto;
                                })));
    }
}

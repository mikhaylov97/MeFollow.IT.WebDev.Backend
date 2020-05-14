package com.mefollow.webschool.checker.usecase.CheckAnswer;

import com.mefollow.webschool.checker.application.RuleCheckerService;
import com.mefollow.webschool.checker.domain.dto.CheckerResultDto;
import com.mefollow.webschool.sandbox.domain.base.SandboxResource;
import com.mefollow.webschool.sandbox.infrastructure.repository.ProgressRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceBundleRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static com.mefollow.webschool.sandbox.domain.base.SandboxResourceType.*;
import static reactor.core.publisher.Mono.error;

@Service
public class CheckAnswerHandler {

    private ProgressRepository progressRepository;
    private RuleCheckerService ruleCheckerService;
    private SandboxResourceRepository sandboxResourceRepository;
    private SandboxResourceBundleRepository sandboxResourceBundleRepository;

    public CheckAnswerHandler(ProgressRepository progressRepository,
                              RuleCheckerService ruleCheckerService,
                              SandboxResourceRepository sandboxResourceRepository,
                              SandboxResourceBundleRepository sandboxResourceBundleRepository) {
        this.progressRepository = progressRepository;
        this.ruleCheckerService = ruleCheckerService;
        this.sandboxResourceRepository = sandboxResourceRepository;
        this.sandboxResourceBundleRepository = sandboxResourceBundleRepository;
    }

    public Mono<CheckerResultDto> handle(CheckAnswerCommand command) {
        return sandboxResourceBundleRepository.findByChapterIdAndUserId(command.getChapterId(), command.getUserId())
                .switchIfEmpty(error(SANDBOX_RESOURCE_BUNDLE_NOT_FOUND))
                .flatMap(bundle -> sandboxResourceRepository.findAllByBundleId(bundle.getId())
                        .collectList()
                        .flatMap(resources -> {
                            if (resources.isEmpty()) return error(SANDBOX_RESOURCES_INCONSISTENCY);

                            final var htmlContent = resources.stream().filter(resource -> HTML == resource.getType()).findFirst();
                            if (htmlContent.isEmpty()) return error(SANDBOX_RESOURCES_INCONSISTENCY);

                            final var cssContent = resources.stream().filter(resource -> CSS == resource.getType()).findFirst();
                            final var jsContent = resources.stream().filter(resource -> JS == resource.getType()).findFirst();
                            return ruleCheckerService.checkRules(command.getChapterId(), htmlContent.get().getContent(), cssContent.map(SandboxResource::getContent).orElse(null), jsContent.map(SandboxResource::getContent).orElse(null));
                        }))
                .flatMap(result -> progressRepository.findFirstByUserIdAndChapterId(command.getUserId(), command.getChapterId())
                        .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                        .flatMap(progress -> {
                            progress.setSolved(true);
                            return progressRepository.save(progress).thenReturn(result);
                        }));
    }
}

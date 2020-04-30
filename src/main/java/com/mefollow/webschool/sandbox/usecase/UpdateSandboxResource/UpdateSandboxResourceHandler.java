package com.mefollow.webschool.sandbox.usecase.UpdateSandboxResource;

import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.SANDBOX_RESOURCE_NOT_FOUND;
import static reactor.core.publisher.Mono.error;

@Service
public class UpdateSandboxResourceHandler {

    private final SandboxResourceRepository sandboxResourceRepository;

    public UpdateSandboxResourceHandler(SandboxResourceRepository sandboxResourceRepository) {
        this.sandboxResourceRepository = sandboxResourceRepository;
    }

    public Mono<Void> handle(final UpdateSandboxResourceCommand command) {
        return sandboxResourceRepository.findByBundleIdAndType(command.getBundleId(), command.getType())
                .switchIfEmpty(error(SANDBOX_RESOURCE_NOT_FOUND))
                .flatMap(resourceToBeUpdated -> {
                    resourceToBeUpdated.setContent(command.getContent());
                    return sandboxResourceRepository.save(resourceToBeUpdated);
                })
                .then();
    }
}

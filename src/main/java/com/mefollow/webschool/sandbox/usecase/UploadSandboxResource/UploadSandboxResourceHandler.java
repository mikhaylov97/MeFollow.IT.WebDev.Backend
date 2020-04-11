package com.mefollow.webschool.sandbox.usecase.UploadSandboxResource;

import com.mefollow.webschool.sandbox.domain.SandboxResource;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UploadSandboxResourceHandler {

    private final SandboxResourceRepository sandboxResourceRepository;

    public UploadSandboxResourceHandler(SandboxResourceRepository sandboxResourceRepository) {
        this.sandboxResourceRepository = sandboxResourceRepository;
    }

    public Mono<Void> handle(final UploadSandboxResourceCommand command) {
        final var sandboxResource = new SandboxResource(command.getUserId(), command.getContent(), command.getType());
        return sandboxResourceRepository.save(sandboxResource)
                .then();
    }
}

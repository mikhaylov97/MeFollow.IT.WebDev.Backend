package com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource;

import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.SandboxResourceException.SANDBOX_RESOURCE_FORBIDDEN;
import static com.mefollow.webschool.sandbox.domain.SandboxResourceException.SANDBOX_RESOURCE_NOT_FOUND;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class DeleteSandboxResourceHandler {

    private final UserRepository userRepository;
    private final SandboxResourceRepository sandboxResourceRepository;

    public DeleteSandboxResourceHandler(UserRepository userRepository,
                                        SandboxResourceRepository sandboxResourceRepository) {
        this.userRepository = userRepository;
        this.sandboxResourceRepository = sandboxResourceRepository;
    }

    public Mono<Void> handle(final DeleteSandboxResourceCommand command) {
        return sandboxResourceRepository.findById(command.getSandboxResourceId())
                .switchIfEmpty(error(SANDBOX_RESOURCE_NOT_FOUND))
                .flatMap(sandboxResource -> userRepository.findById(sandboxResource.getUserId())
                        .flatMap(sandboxResourceOwner -> {
                            if (sandboxResourceOwner.getId().equals(command.getUserId()) || command.getUser().isGlobalAdmin()) return just(sandboxResource);
                            else return error(SANDBOX_RESOURCE_FORBIDDEN);
                        }))
                .flatMap(sandboxResourceRepository::delete);
    }
}

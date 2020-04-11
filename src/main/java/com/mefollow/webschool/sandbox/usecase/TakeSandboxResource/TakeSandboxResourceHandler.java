package com.mefollow.webschool.sandbox.usecase.TakeSandboxResource;

import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import com.mefollow.webschool.sandbox.domain.SandboxResource;
import com.mefollow.webschool.sandbox.infrastructure.repository.SandboxResourceRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.SandboxResourceException.*;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class TakeSandboxResourceHandler {

    private final UserRepository userRepository;
    private final SandboxResourceRepository sandboxResourceRepository;

    public TakeSandboxResourceHandler(UserRepository userRepository,
                                      SandboxResourceRepository sandboxResourceRepository) {
        this.userRepository = userRepository;
        this.sandboxResourceRepository = sandboxResourceRepository;
    }

    public Mono<ResponseEntity<byte[]>> handle(final TakeSandboxResourceCommand command) {
        return sandboxResourceRepository.findById(command.getSandboxResourceId())
                .switchIfEmpty(error(SANDBOX_RESOURCE_NOT_FOUND))
                .flatMap(sandboxResource -> userRepository.findById(sandboxResource.getUserId())
                        .flatMap(sandboxResourceOwner -> {
                            if (sandboxResourceOwner.getId().equals(command.getUserId()) || command.getUser().isGlobalAdmin()) return just(sandboxResource);
                            else return error(SANDBOX_RESOURCE_FORBIDDEN);
                        }))
                .flatMap(sandboxResource -> constructHttpHeadersBySandboxResourceType(sandboxResource)
                        .map(httpHeader -> new ResponseEntity<>(sandboxResource.getContent().getBytes(), httpHeader, HttpStatus.OK)));
    }

    private Mono<HttpHeaders> constructHttpHeadersBySandboxResourceType(final SandboxResource sandboxResource) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentLength(sandboxResource.getContent().getBytes().length);
        switch (sandboxResource.getType()) {
            case JS:
                headers.setContentType(MediaType.valueOf("application/javascript; charset=utf-8"));
                break;
            case CSS:
                headers.setContentType(MediaType.valueOf("text/css; charset=utf-8"));
                break;
            case HTML:
                headers.setContentType(MediaType.valueOf("text/html; charset=utf-8"));
                break;
            default: return error(WRONG_SANDBOX_RESOURCE_TYPE);
        }

        return just(headers);
    }
}

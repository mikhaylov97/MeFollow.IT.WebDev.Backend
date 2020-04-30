package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.SandboxResource;
import com.mefollow.webschool.sandbox.domain.base.SandboxResourceType;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SandboxResourceRepository extends AbstractRepository<SandboxResource> {
    Flux<SandboxResource> findAllByBundleId(String bundleId);
    Mono<SandboxResource> findByBundleIdAndType(String bundleId, SandboxResourceType type);
}

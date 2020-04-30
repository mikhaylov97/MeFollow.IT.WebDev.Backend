package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.SandboxResourceBundle;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SandboxResourceBundleRepository extends AbstractRepository<SandboxResourceBundle> {
    Mono<SandboxResourceBundle> findByChapterIdAndUserId(String chapterId, String userId);
}

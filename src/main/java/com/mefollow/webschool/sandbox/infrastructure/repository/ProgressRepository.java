package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.Progress;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProgressRepository extends AbstractRepository<Progress> {
    Mono<Progress> findFirstByUserIdAndChapterId(String userId, String chapterId);
    Mono<Progress> findFirstByUserIdAndCourseId(String userId, String courseId);
}

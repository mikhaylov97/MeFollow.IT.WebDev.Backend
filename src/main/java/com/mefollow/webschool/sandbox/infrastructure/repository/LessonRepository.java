package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.Lesson;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LessonRepository extends AbstractRepository<Lesson> {
    Mono<Lesson> findFirstByCourseIdOrderByOrderIndex(String courseId);
}

package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.Topic;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TopicRepository extends AbstractRepository<Topic> {
    Mono<Boolean> existsTopicByCourseIdAndOrderIndex(String courseId, int orderIndex);
    Mono<Topic> findFirstByCourseIdOrderByOrderIndex(String courseId);
    Flux<Topic> findAllByCourseIdOrderByOrderIndex(String courseId);
}

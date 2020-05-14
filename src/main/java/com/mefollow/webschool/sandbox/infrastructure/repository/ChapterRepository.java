package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.Chapter;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChapterRepository extends AbstractRepository<Chapter> {
    Mono<Boolean> existsChapterByLessonIdAndOrderIndex(String lessonId, int orderIndex);
    Flux<Chapter> findAllByLessonIdOrderByOrderIndex(String lessonId);
    Flux<Chapter> findAllByLessonIdAndOrderIndexIsLessThanEqualOrderByOrderIndex(String lessonId, int orderIndex);
    Mono<Chapter> findFirstByLessonIdOrderByOrderIndex(String lessonId);
    Mono<Long> countAllByLessonId(String lessonId);
}

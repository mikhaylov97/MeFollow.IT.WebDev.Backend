package com.mefollow.webschool.checker.infrastructure.repository;

import com.mefollow.webschool.checker.domain.base.CheckRule;
import com.mefollow.webschool.checker.domain.base.CheckRuleType;
import com.mefollow.webschool.core.repository.AbstractRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CheckRuleRepository extends AbstractRepository<CheckRule> {
    Flux<CheckRule> findAllByChapterId(String chapterId);
    Flux<CheckRule> findAllByChapterIdAndType(String chapterId, CheckRuleType type);
}

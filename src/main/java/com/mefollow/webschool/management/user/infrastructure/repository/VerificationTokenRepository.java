package com.mefollow.webschool.management.user.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.management.user.domain.auth.VerificationToken;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VerificationTokenRepository extends AbstractRepository<VerificationToken> {
    Mono<VerificationToken> findFirstByToken(String token);
    Mono<VerificationToken> deleteByToken(String token);
    Flux<VerificationToken> deleteAllByUserId(String userId);
}

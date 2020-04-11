package com.mefollow.webschool.core.security.token;

import com.mefollow.webschool.core.repository.AbstractRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TokenRepository extends AbstractRepository<Token> {
    Mono<Token> findByToken(String token);
    Flux<Token> deleteAllByUserId(String id);
}

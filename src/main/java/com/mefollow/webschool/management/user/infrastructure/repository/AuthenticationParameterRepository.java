package com.mefollow.webschool.management.user.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.management.user.domain.auth.AuthenticationParameter;
import com.mefollow.webschool.management.user.domain.auth.AuthenticationProvider;
import com.mefollow.webschool.management.user.domain.auth.IndirectAuthenticationParameter;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthenticationParameterRepository extends AbstractRepository<AuthenticationParameter> {
    Mono<AuthenticationParameter> findFirstByUserIdAndAuthenticationProvider(String userId, AuthenticationProvider provider);
    Mono<IndirectAuthenticationParameter> findFirstByExternalIdAndAuthenticationProvider(String externalId, AuthenticationProvider provider);
    Flux<IndirectAuthenticationParameter> findAllByUserId(String userId);

    Mono<Long> countByUserId(String userId);
    Mono<Boolean> existsByUserIdAndAuthenticationProvider(String userId, AuthenticationProvider provider);

    Mono<Long> deleteByUserIdAndAuthenticationProvider(String userId, AuthenticationProvider provider);
}

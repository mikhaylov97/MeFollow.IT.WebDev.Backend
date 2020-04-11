package com.mefollow.webschool.management.user.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.management.user.domain.account.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends AbstractRepository<User> {
    //common
    Mono<User> findFirstByEmailIgnoreCase(String email);
    Mono<User> findFirstByEmailIgnoreCaseAndBannedFalse(String email);
    Mono<User> findFirstByIdAndBannedFalse(String id);
    Flux<User> findByIdIn(List<String> ids);
    Flux<User> findByIdInOrderByName(List<String> ids);

    //global admins
    Flux<User> findByGlobalAdminIsTrue();
    Flux<User> findByIdInAndGlobalAdminIsTrue(Collection<String> ids);

    //util
    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<Long> countAllByBannedIsTrue();
}

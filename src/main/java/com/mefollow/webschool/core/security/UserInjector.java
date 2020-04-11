package com.mefollow.webschool.core.security;

import com.mefollow.webschool.core.domain.DomainCommand;
import com.mefollow.webschool.core.exception.CoreException;
import com.mefollow.webschool.management.user.domain.account.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.mefollow.webschool.core.domain.DomainCommand.injectUser;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static reactor.core.publisher.Mono.error;

public class UserInjector {

    public static <T, D extends DomainCommand> Mono<T> injectPrincipalAndThen(D command, Function<D, Mono<T>> handler) {
        return SecurityContextRepository.loggedUser()
                .map(injectUser(command))
                .switchIfEmpty(error(new CoreException(UNAUTHORIZED)))
                .flatMap(handler)
                .onErrorResume(Mono::error);
    }

    public static <T> Flux<T> injectPrincipalIdAndFlux(Function<String, Flux<T>> handler) {
        return SecurityContextRepository.loggedUserId()
                .switchIfEmpty(error(new CoreException(UNAUTHORIZED)))
                .flatMapMany(handler)
                .onErrorResume(Mono::error);
    }

    public static <T> Flux<T> injectPrincipalAndFlux(Function<User, Flux<T>> handler) {
        return SecurityContextRepository.loggedUser()
                .switchIfEmpty(error(new CoreException(UNAUTHORIZED)))
                .flatMapMany(handler)
                .onErrorResume(Mono::error);
    }

    public static <T> Mono<T> injectPrincipalAndMono(Function<User, Mono<T>> handler) {
        return SecurityContextRepository.loggedUser()
                .switchIfEmpty(error(new CoreException(UNAUTHORIZED)))
                .flatMap(handler);
    }
}

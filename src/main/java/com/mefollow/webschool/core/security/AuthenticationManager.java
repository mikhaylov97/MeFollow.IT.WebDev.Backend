package com.mefollow.webschool.core.security;

import com.mefollow.webschool.core.security.token.Token;
import com.mefollow.webschool.core.security.token.TokenRepository;
import com.mefollow.webschool.management.user.domain.account.User;
import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.core.security.Role.ROLE_ACCESSOR;
import static com.mefollow.webschool.core.security.Role.ROLE_REFRESHER;
import static com.mefollow.webschool.core.security.token.TokenType.REFRESH;
import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static reactor.core.publisher.Mono.just;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public AuthenticationManager(UserRepository userRepository,
                                 TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        return tokenRepository.findByToken(authToken)
                .filter(token -> now().isBefore(token.getExpireAt()))
                .flatMap(token -> {
                    if (REFRESH == token.getType()) return just(getRefresherAuth(token));
                    return userRepository.findById(token.getUserId()).map(this::getAccessAuth);
                });
    }

    private UsernamePasswordAuthenticationToken getAccessAuth(User user) {
        return new UsernamePasswordAuthenticationToken(user, null, singleton(new SimpleGrantedAuthority(ROLE_ACCESSOR.name())));
    }

    private UsernamePasswordAuthenticationToken getRefresherAuth(Token token) {
        return new UsernamePasswordAuthenticationToken(token.getUserId(), null, singleton(new SimpleGrantedAuthority(ROLE_REFRESHER.name())));
    }
}

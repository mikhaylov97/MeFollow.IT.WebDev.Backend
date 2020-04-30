package com.mefollow.webschool.management.user.usecase.LoginViaForm;

import com.mefollow.webschool.core.domain.BaseModel;
import com.mefollow.webschool.core.security.token.Token;
import com.mefollow.webschool.core.security.token.TokenRepository;
import com.mefollow.webschool.management.user.domain.auth.DirectAuthenticationParameter;
import com.mefollow.webschool.management.user.domain.auth.VerificationToken;
import com.mefollow.webschool.management.user.infrastructure.repository.AuthenticationParameterRepository;
import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import com.mefollow.webschool.management.user.infrastructure.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static com.mefollow.webschool.core.domain.AdditionalFluidFields.*;
import static com.mefollow.webschool.core.json.ObjectMapper.model;
import static com.mefollow.webschool.core.security.token.Token.tokensFor;
import static com.mefollow.webschool.management.user.domain.AccountManagementException.UNAUTHORIZED;
import static com.mefollow.webschool.management.user.domain.auth.AuthenticationProvider.PASSWORD;
import static java.util.UUID.randomUUID;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class LoginViaFormHandler {

    @Value("${app.default.password:12345678}")
    private String defaultPassword;
    @Value("#{T(java.time.Duration).parse('${app.verification.token.lifetime:PT60M}')}")
    private Duration verificationTokenLifetime;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationParameterRepository authenticationParameterRepository;

    public LoginViaFormHandler(UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               TokenRepository tokenRepository,
                               VerificationTokenRepository verificationTokenRepository,
                               AuthenticationParameterRepository authenticationParameterRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationParameterRepository = authenticationParameterRepository;
    }

    public Mono<BaseModel> handle(final LoginViaFormCommand command) {
        final var model = model();
        return userRepository.findFirstByEmailIgnoreCaseAndBannedFalse(command.getEmail())
                .flatMap(user -> authenticationParameterRepository.findByUserIdAndAuthenticationProvider(user.getId(), PASSWORD)
                        .cast(DirectAuthenticationParameter.class)
                        .flatMap(authenticationParameter ->
                                user.isDefaultPassword()
                                        ? processFirstLoginWithDefaultPassword(authenticationParameter.getUserId(), command.getPassword())
                                        : processLogin(authenticationParameter, command))
                        .flatMap(tokens -> {
                            if (!user.isDefaultPassword()) return just(tokens);

                            final var token = randomUUID().toString();
                            return verificationTokenRepository.save(new VerificationToken(user.getId(), token, verificationTokenLifetime))
                                    .flatMap(verificationToken -> {
                                        model.set(VERIFICATION_TOKEN, token);
                                        model.set(DEFAULT_PASSWORD, true);
                                        return just(tokens);
                                    });
                        }))
                .switchIfEmpty(error(UNAUTHORIZED))
                .flatMapMany(this::renewTokens)
                .collectList()
                .map(tokens -> model.set(TOKENS, tokens));
    }

    private Flux<Token> renewTokens(List<Token> tokens) {
        return tokenRepository.deleteAllByUserId(tokens.get(0).getUserId())
                .thenMany(tokenRepository.saveAll(tokens));
    }

    private Mono<List<Token>> processLogin(final DirectAuthenticationParameter authenticationParameter, final LoginViaFormCommand command) {
        return passwordEncoder.matches(command.getPassword(), authenticationParameter.getPassword()) ? tokensFor(authenticationParameter.getUserId()) : error(UNAUTHORIZED);
    }

    private Mono<List<Token>> processFirstLoginWithDefaultPassword(String accountId, String password) {
        return defaultPassword.equalsIgnoreCase(password) ? tokensFor(accountId) : error(UNAUTHORIZED);
    }
}

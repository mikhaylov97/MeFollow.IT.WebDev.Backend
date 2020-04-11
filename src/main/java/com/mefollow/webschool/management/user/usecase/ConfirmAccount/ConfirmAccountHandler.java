package com.mefollow.webschool.management.user.usecase.ConfirmAccount;

import com.mefollow.webschool.core.security.token.Token;
import com.mefollow.webschool.core.security.token.TokenRepository;
import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import com.mefollow.webschool.management.user.infrastructure.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.mefollow.webschool.management.user.domain.AccountManagementException.GONE;
import static com.mefollow.webschool.management.user.domain.account.UserAccountStatus.CONFIRMED;
import static java.time.LocalDateTime.now;
import static reactor.core.publisher.Mono.error;

@Service
public class ConfirmAccountHandler {

    //TODO: implement notification service

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Value("#{T(java.time.Duration).parse('${app.verification.token.lifetime:PT60M}')}")
    private Duration verificationTokenLifetime;

    @Value("${app.host.url:http://localhost:8080}")
    private String hostUrl;

    @Autowired
    public ConfirmAccountHandler(UserRepository userRepository,
                                 TokenRepository tokenRepository,
                                 VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public Flux<Token> hanlde(final ConfirmAccountCommand command) {
        return verificationTokenRepository
                .findFirstByToken(command.getVerificationToken())
                .switchIfEmpty(error(GONE))
                .flatMap(verificationToken -> verificationTokenRepository.deleteByToken(command.getVerificationToken())
                        .thenReturn(verificationToken))
                .flatMap(verificationToken -> {
                    if (now().isAfter(verificationToken.getExpireAt())) {
                        return error(GONE);
                    }

                    return userRepository.findById(verificationToken.getUserId())
                            .flatMap(user -> {
                                user.setStatus(CONFIRMED);
                                return userRepository.save(user);
                            });
                })
                .flatMapMany(user -> renewTokens(user.getId()));
    }

    private Flux<Token> renewTokens(String userId) {
        return Token.tokensFor(userId)
                .flatMapMany(tokens -> tokenRepository.deleteAllByUserId(userId)
                        .thenMany(tokenRepository.saveAll(tokens)));
    }
}

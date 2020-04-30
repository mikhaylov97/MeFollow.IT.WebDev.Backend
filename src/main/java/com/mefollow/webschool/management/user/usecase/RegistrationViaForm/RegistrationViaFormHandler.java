package com.mefollow.webschool.management.user.usecase.RegistrationViaForm;

import com.mefollow.webschool.core.domain.BaseModel;
import com.mefollow.webschool.management.user.domain.auth.DirectAuthenticationParameter;
import com.mefollow.webschool.management.user.infrastructure.repository.AuthenticationParameterRepository;
import com.mefollow.webschool.management.user.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.core.domain.AdditionalFluidFields.TOKENS;
import static com.mefollow.webschool.core.json.ObjectMapper.model;
import static com.mefollow.webschool.core.security.token.Token.tokensFor;
import static com.mefollow.webschool.management.user.domain.AccountManagementException.USER_ALREADY_EXIST;
import static com.mefollow.webschool.management.user.domain.account.User.createUser;
import static reactor.core.publisher.Mono.error;

@Service
public class RegistrationViaFormHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationParameterRepository authenticationParameterRepository;

    public RegistrationViaFormHandler(UserRepository userRepository,
                                      PasswordEncoder passwordEncoder,
                                      AuthenticationParameterRepository authenticationParameterRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationParameterRepository = authenticationParameterRepository;
    }

    public Mono<BaseModel> handle(final RegistrationViaFormCommand command) {
        return userRepository.existsByEmailIgnoreCase(command.getEmail())
                .flatMap(isUserExist -> isUserExist
                        ? error(USER_ALREADY_EXIST)
                        : userRepository.save(createUser(command.getEmail(), command.getName(), command.getLanguage())))
                .flatMap(user -> authenticationParameterRepository.save(new DirectAuthenticationParameter(user.getId(), passwordEncoder.encode(command.getPassword()))))
                .flatMap(authenticationParameter -> tokensFor(authenticationParameter.getUserId()))
                .map(tokens -> model().set(TOKENS, tokens));
                                //.thenReturn(user));
                                    //TODO: send email with confirmation token
                                    //.flatMap(directAuthenticationParameter -> confirmAccountHandler.createRegistrationVerifyTokenAndSendEmail(account, null))
                                    //.thenReturn(account)
    }
}

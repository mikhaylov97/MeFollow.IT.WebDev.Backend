package com.mefollow.webschool.management.user.infrastructure.controller;

import com.mefollow.webschool.core.domain.BaseModel;
import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.core.security.token.Token;
import com.mefollow.webschool.management.user.domain.account.User;
import com.mefollow.webschool.management.user.usecase.ConfirmAccount.ConfirmAccountCommand;
import com.mefollow.webschool.management.user.usecase.ConfirmAccount.ConfirmAccountHandler;
import com.mefollow.webschool.management.user.usecase.LoginViaForm.LoginViaFormCommand;
import com.mefollow.webschool.management.user.usecase.LoginViaForm.LoginViaFormHandler;
import com.mefollow.webschool.management.user.usecase.RegistrationViaForm.RegistrationViaFormCommand;
import com.mefollow.webschool.management.user.usecase.RegistrationViaForm.RegistrationViaFormHandler;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

//@Api(description = "Менеджмент - пользовательские аккаунты")
@RestController
@RequestMapping("account")
public class UserController extends ExceptionInterceptor {

    private final LoginViaFormHandler loginViaFormHandler;
    private final ConfirmAccountHandler confirmAccountHandler;
    private final RegistrationViaFormHandler registrationViaFormHandler;

    public UserController(LoginViaFormHandler loginViaFormHandler,
                          ConfirmAccountHandler confirmAccountHandler,
                          RegistrationViaFormHandler registrationViaFormHandler) {
        this.loginViaFormHandler = loginViaFormHandler;
        this.confirmAccountHandler = confirmAccountHandler;
        this.registrationViaFormHandler = registrationViaFormHandler;
    }

    //@ApiOperation(value = "Полученый в ответе ACCESS-токен отправлять в хедере `AUTHORIZATION` с приставкой `Bearer `")
    @PostMapping(value = "/login")
    public Mono<BaseModel> login(@RequestBody @Valid LoginViaFormCommand command) {
        return loginViaFormHandler.handle(command);
    }

    @PostMapping(value = "/registration")
    public Mono<BaseModel> registration(@RequestBody @Valid RegistrationViaFormCommand command) {
        return registrationViaFormHandler.handle(command);
    }

    //@ApiOperation(value = "Подтвердить email")
    @GetMapping(value = "/confirm")
    public Flux<Token> confirm(@RequestParam("token") String verificationToken) {
        final var command = new ConfirmAccountCommand();
        command.setVerificationToken(verificationToken);
        return confirmAccountHandler.hanlde(command);
    }

    //TODO: implement complete registration and etc. API
}

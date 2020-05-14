package com.mefollow.webschool.checker.infrastructure.controller;

import com.mefollow.webschool.checker.domain.dto.CheckerResultDto;
import com.mefollow.webschool.checker.usecase.CheckAnswer.CheckAnswerCommand;
import com.mefollow.webschool.checker.usecase.CheckAnswer.CheckAnswerHandler;
import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "sandbox", headers = AUTHORIZATION)
public class CheckerController extends ExceptionInterceptor {

    private CheckAnswerHandler checkAnswerHandler;

    public CheckerController(CheckAnswerHandler checkAnswerHandler) {
        this.checkAnswerHandler = checkAnswerHandler;
    }

    @PostMapping(value = "/chapter/{id}/check")
    public Mono<CheckerResultDto> updateSandboxResource(@PathVariable("id") String chapterId) {
        final var command = new CheckAnswerCommand();
        command.setChapterId(chapterId);
        return injectPrincipalAndThen(command, checkAnswerHandler::handle);
    }
}

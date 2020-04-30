package com.mefollow.webschool.sandbox.infrastructure.controller;

import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.sandbox.domain.dto.SandboxResourceBundleDto;
import com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource.DeleteSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource.DeleteSandboxResourceHandler;
import com.mefollow.webschool.sandbox.usecase.ResetChapterProgress.ResetChapterProgressCommand;
import com.mefollow.webschool.sandbox.usecase.ResetChapterProgress.ResetChapterProgressHandler;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResource.TakeSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResource.TakeSandboxResourceHandler;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResourceBundleAsHtml.TakeSandboxResourceBundleAsHtmlCommand;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResourceBundleAsHtml.TakeSandboxResourceBundleAsHtmlHandler;
import com.mefollow.webschool.sandbox.usecase.UpdateSandboxResource.UpdateSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.UpdateSandboxResource.UpdateSandboxResourceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

//TODO: import swagger
@RestController
//TODO: add authorization header
@RequestMapping("sandbox")
public class SandboxController extends ExceptionInterceptor {

    private final TakeSandboxResourceHandler takeSandboxResourceHandler;
    private final ResetChapterProgressHandler resetChapterProgressHandler;
    private final UpdateSandboxResourceHandler updateSandboxResourceHandler;
    private final DeleteSandboxResourceHandler deleteSandboxResourceHandler;
    private final TakeSandboxResourceBundleAsHtmlHandler takeSandboxResourceBundleAsHtmlHandler;

    public SandboxController(TakeSandboxResourceHandler takeSandboxResourceHandler,
                             ResetChapterProgressHandler resetChapterProgressHandler,
                             UpdateSandboxResourceHandler updateSandboxResourceHandler,
                             DeleteSandboxResourceHandler deleteSandboxResourceHandler,
                             TakeSandboxResourceBundleAsHtmlHandler takeSandboxResourceBundleAsHtmlHandler) {
        this.takeSandboxResourceHandler = takeSandboxResourceHandler;
        this.resetChapterProgressHandler = resetChapterProgressHandler;
        this.updateSandboxResourceHandler = updateSandboxResourceHandler;
        this.deleteSandboxResourceHandler = deleteSandboxResourceHandler;
        this.takeSandboxResourceBundleAsHtmlHandler = takeSandboxResourceBundleAsHtmlHandler;
    }

    @GetMapping(value = "/bundle/{id}")
    public Mono<ResponseEntity<byte[]>> takeSandboxResourceBundleAsHtml(@PathVariable("id") String bundleId) {
        final var command = new TakeSandboxResourceBundleAsHtmlCommand();
        command.setBundleId(bundleId);
        return takeSandboxResourceBundleAsHtmlHandler.handle(command);
    }


    @GetMapping("/bundle/resource/{id}")
    public Mono<ResponseEntity<byte[]>> takeSandboxResource(@PathVariable("id") String sandboxResourceId) {
        final var command = new TakeSandboxResourceCommand();
        command.setSandboxResourceId(sandboxResourceId);
        return takeSandboxResourceHandler.handle(command);
    }

    @PostMapping(value = "/bundle/{id}", headers = AUTHORIZATION)
    public Mono<Void> updateSandboxResource(@PathVariable("id") String bundleId,
                                            @RequestBody @Valid UpdateSandboxResourceCommand command) {
        return injectPrincipalAndThen(command.setBundleId(bundleId), updateSandboxResourceHandler::handle);
    }

    @DeleteMapping(value = "/{id}", headers = AUTHORIZATION)
    public Mono<Void> deleteSandboxResource(@PathVariable("id") String sandboxResourceId) {
        final var command = new DeleteSandboxResourceCommand();
        command.setSandboxResourceId(sandboxResourceId);
        return injectPrincipalAndThen(command, deleteSandboxResourceHandler::handle);
    }

    @PostMapping(value = "/bundle/{id}/reset")
    public Mono<SandboxResourceBundleDto> resetChapterProgress(@PathVariable(name = "id") String bundleId) {
        final var command = new ResetChapterProgressCommand();
        command.setBundleId(bundleId);
        return injectPrincipalAndThen(command, resetChapterProgressHandler::handle);
    }
}

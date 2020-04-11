package com.mefollow.webschool.sandbox.infrastructure.controller;

import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource.DeleteSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.DeleteSandboxResource.DeleteSandboxResourceHandler;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResource.TakeSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.TakeSandboxResource.TakeSandboxResourceHandler;
import com.mefollow.webschool.sandbox.usecase.UploadSandboxResource.UploadSandboxResourceCommand;
import com.mefollow.webschool.sandbox.usecase.UploadSandboxResource.UploadSandboxResourceHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;

//TODO: import swagger
//@Api(description = "Управление ресурсами для sandbox")
@RestController
//TODO: add authorization header
@RequestMapping(value = "resources/sandbox")
//@RequestMapping(value = "resources", headers = AUTHORIZATION)
public class SandboxController extends ExceptionInterceptor {

    private final TakeSandboxResourceHandler takeSandboxResourceHandler;
    private final UploadSandboxResourceHandler uploadSandboxResourceHandler;
    private final DeleteSandboxResourceHandler deleteSandboxResourceHandler;

    public SandboxController(TakeSandboxResourceHandler takeSandboxResourceHandler,
                             UploadSandboxResourceHandler uploadSandboxResourceHandler,
                             DeleteSandboxResourceHandler deleteSandboxResourceHandler) {
        this.takeSandboxResourceHandler = takeSandboxResourceHandler;
        this.uploadSandboxResourceHandler = uploadSandboxResourceHandler;
        this.deleteSandboxResourceHandler = deleteSandboxResourceHandler;
    }

    //@ApiOperation(value = "Получение ресурсов")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<byte[]>> getSandboxResources(@PathVariable("id") String sandboxResourceId) {
        final var command = new TakeSandboxResourceCommand();
        command.setSandboxResourceId(sandboxResourceId);
        return injectPrincipalAndThen(command, takeSandboxResourceHandler::handle);
    }

    //@ApiOperation(value = "Добавление ресурсов")
    @PostMapping
    public Mono<Void> uploadSandboxResources(@RequestBody @Valid UploadSandboxResourceCommand command) {
        return injectPrincipalAndThen(command, uploadSandboxResourceHandler::handle);
    }

    //@ApiOperation(value = "Удаление ресурсов")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteSandboxResources(@PathVariable("id") String sandboxResourceId) {
        final var command = new DeleteSandboxResourceCommand();
        command.setSandboxResourceId(sandboxResourceId);
        return injectPrincipalAndThen(command, deleteSandboxResourceHandler::handle);
    }
}

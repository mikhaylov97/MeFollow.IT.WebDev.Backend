package com.mefollow.webschool.sandbox.infrastructure.controller;


import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.sandbox.domain.dto.BriefCourseDto;
import com.mefollow.webschool.sandbox.domain.dto.FullCourseDto;
import com.mefollow.webschool.sandbox.domain.dto.LessonDto;
import com.mefollow.webschool.sandbox.usecase.TakeCourse.TakeCourseCommand;
import com.mefollow.webschool.sandbox.usecase.TakeCourse.TakeCourseHandler;
import com.mefollow.webschool.sandbox.usecase.TakeLesson.TakeLessonCommand;
import com.mefollow.webschool.sandbox.usecase.TakeLesson.TakeLessonHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(headers = AUTHORIZATION)
public class CommonController extends ExceptionInterceptor {

    private final TakeLessonHandler takeLessonHandler;
    private final TakeCourseHandler takeCourseHandler;

    public CommonController(TakeLessonHandler takeLessonHandler,
                            TakeCourseHandler takeCourseHandler) {
        this.takeLessonHandler = takeLessonHandler;
        this.takeCourseHandler = takeCourseHandler;
    }

    @GetMapping(value = "/courses")
    public Flux<BriefCourseDto> takeAllCoursesBrief() {
        return takeCourseHandler.takeAllCourses();
    }

    @GetMapping(value = "/courses/{id}")
    public Mono<FullCourseDto> takeCourseByIdFull(@PathVariable(name = "id") String courseId) {
        final var command = new TakeCourseCommand();
        command.setCourseId(courseId);
        return injectPrincipalAndThen(command, takeCourseHandler::takeCourse);
    }

    @GetMapping(value = "/courses/{courseId}/lessons/{lessonId}")
    public Mono<LessonDto> takeLesson(@PathVariable(name = "courseId") String courseId,
                                               @PathVariable(name = "lessonId") String lessonId) {
        final var command = new TakeLessonCommand();
        command.setCourseId(courseId);
        command.setLessonId(lessonId);
        return injectPrincipalAndThen(command, takeLessonHandler::handle);
    }
}

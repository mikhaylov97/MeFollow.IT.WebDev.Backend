package com.mefollow.webschool.sandbox.infrastructure.controller;


import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.sandbox.domain.dto.BriefCourseDto;
import com.mefollow.webschool.sandbox.domain.dto.FullCourseDto;
import com.mefollow.webschool.sandbox.domain.dto.LessonDto;
import com.mefollow.webschool.sandbox.usecase.MoveToNextChapter.MoveToNextChapterCommand;
import com.mefollow.webschool.sandbox.usecase.MoveToNextChapter.MoveToNextChapterHandler;
import com.mefollow.webschool.sandbox.usecase.TakeCourse.TakeCourseCommand;
import com.mefollow.webschool.sandbox.usecase.TakeCourse.TakeCourseHandler;
import com.mefollow.webschool.sandbox.usecase.TakeLesson.TakeLessonCommand;
import com.mefollow.webschool.sandbox.usecase.TakeLesson.TakeLessonHandler;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(headers = AUTHORIZATION)
public class CommonController extends ExceptionInterceptor {

    private final TakeLessonHandler takeLessonHandler;
    private final TakeCourseHandler takeCourseHandler;
    private final MoveToNextChapterHandler moveToNextChapterHandler;

    public CommonController(TakeLessonHandler takeLessonHandler,
                            TakeCourseHandler takeCourseHandler,
                            MoveToNextChapterHandler moveToNextChapterHandler) {
        this.takeLessonHandler = takeLessonHandler;
        this.takeCourseHandler = takeCourseHandler;
        this.moveToNextChapterHandler = moveToNextChapterHandler;
    }

    @GetMapping("/courses")
    public Flux<BriefCourseDto> takeAllCoursesBrief() {
        return takeCourseHandler.takeAllCourses();
    }

    @GetMapping("/courses/{id}")
    public Mono<FullCourseDto> takeCourseByIdFull(@PathVariable(name = "id") String courseId) {
        final var command = new TakeCourseCommand();
        command.setCourseId(courseId);
        return injectPrincipalAndThen(command, takeCourseHandler::takeCourse);
    }

    @GetMapping("/courses/{courseId}/lessons/{lessonId}")
    public Mono<LessonDto> takeLesson(@PathVariable(name = "courseId") String courseId,
                                      @PathVariable(name = "lessonId") String lessonId) {
        final var command = new TakeLessonCommand();
        command.setCourseId(courseId);
        command.setLessonId(lessonId);
        return injectPrincipalAndThen(command, takeLessonHandler::handle);
    }

    @PostMapping("/courses/{id}/progress/next")
    public Mono<Void> moveToNextChapter(@PathVariable(name = "id") String courseId) {
        final var command = new MoveToNextChapterCommand();
        command.setCourseId(courseId);
        return injectPrincipalAndThen(command, moveToNextChapterHandler::handle);
    }
}

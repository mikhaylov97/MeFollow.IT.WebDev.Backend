package com.mefollow.webschool.management.admin.infrastructure.controller;

import com.mefollow.webschool.checker.domain.base.CheckRule;
import com.mefollow.webschool.core.exception.ExceptionInterceptor;
import com.mefollow.webschool.management.admin.domain.dto.*;
import com.mefollow.webschool.management.admin.usecase.CreateChapter.CreateChapterCommand;
import com.mefollow.webschool.management.admin.usecase.CreateChapter.CreateChapterHandler;
import com.mefollow.webschool.management.admin.usecase.CreateLesson.CreateLessonCommand;
import com.mefollow.webschool.management.admin.usecase.CreateLesson.CreateLessonHandler;
import com.mefollow.webschool.management.admin.usecase.CreateRule.CreateRuleCommand;
import com.mefollow.webschool.management.admin.usecase.CreateRule.CreateRuleHandler;
import com.mefollow.webschool.management.admin.usecase.CreateTopic.CreateTopicCommand;
import com.mefollow.webschool.management.admin.usecase.CreateTopic.CreateTopicHandler;
import com.mefollow.webschool.management.admin.usecase.DeleteChapter.DeleteChapterCommand;
import com.mefollow.webschool.management.admin.usecase.DeleteChapter.DeleteChapterHandler;
import com.mefollow.webschool.management.admin.usecase.DeleteLesson.DeleteLessonCommand;
import com.mefollow.webschool.management.admin.usecase.DeleteLesson.DeleteLessonHandler;
import com.mefollow.webschool.management.admin.usecase.DeleteRule.DeleteRuleCommand;
import com.mefollow.webschool.management.admin.usecase.DeleteRule.DeleteRuleHandler;
import com.mefollow.webschool.management.admin.usecase.DeleteTopic.DeleteTopicCommand;
import com.mefollow.webschool.management.admin.usecase.DeleteTopic.DeleteTopicHandler;
import com.mefollow.webschool.management.admin.usecase.TakeChapters.TakeChaptersCommand;
import com.mefollow.webschool.management.admin.usecase.TakeChapters.TakeChaptersHandler;
import com.mefollow.webschool.management.admin.usecase.TakeCourses.TakeCoursesHandler;
import com.mefollow.webschool.management.admin.usecase.TakeLessons.TakeLessonsCommand;
import com.mefollow.webschool.management.admin.usecase.TakeLessons.TakeLessonsHandler;
import com.mefollow.webschool.management.admin.usecase.TakeRules.TakeRulesCommand;
import com.mefollow.webschool.management.admin.usecase.TakeRules.TakeRulesHandler;
import com.mefollow.webschool.management.admin.usecase.TakeTopics.TakeTopicsCommand;
import com.mefollow.webschool.management.admin.usecase.TakeTopics.TakeTopicsHandler;
import com.mefollow.webschool.sandbox.domain.base.Chapter;
import com.mefollow.webschool.sandbox.domain.base.Lesson;
import com.mefollow.webschool.sandbox.domain.base.Topic;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndMono;
import static com.mefollow.webschool.core.security.UserInjector.injectPrincipalAndThen;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(value = "management", headers = AUTHORIZATION)
public class ManagementController extends ExceptionInterceptor {

    private final TakeRulesHandler takeRulesHandler;
    private final CreateRuleHandler createRuleHandler;
    private final DeleteRuleHandler deleteRuleHandler;
    private final TakeTopicsHandler takeTopicsHandler;
    private final TakeLessonsHandler takeLessonsHandler;
    private final CreateTopicHandler createTopicHandler;
    private final DeleteTopicHandler deleteTopicHandler;
    private final TakeCoursesHandler takeCoursesHandler;
    private final CreateLessonHandler createLessonHandler;
    private final DeleteLessonHandler deleteLessonHandler;
    private final TakeChaptersHandler takeChaptersHandler;
    private final CreateChapterHandler createChapterHandler;
    private final DeleteChapterHandler deleteChapterHandler;

    public ManagementController(TakeRulesHandler takeRulesHandler,
                                CreateRuleHandler createRuleHandler,
                                DeleteRuleHandler deleteRuleHandler,
                                TakeTopicsHandler takeTopicsHandler,
                                TakeLessonsHandler takeLessonsHandler,
                                CreateTopicHandler createTopicHandler,
                                DeleteTopicHandler deleteTopicHandler,
                                TakeCoursesHandler takeCoursesHandler,
                                CreateLessonHandler createLessonHandler,
                                DeleteLessonHandler deleteLessonHandler,
                                TakeChaptersHandler takeChaptersHandler,
                                CreateChapterHandler createChapterHandler,
                                DeleteChapterHandler deleteChapterHandler) {
        this.takeRulesHandler = takeRulesHandler;
        this.createRuleHandler = createRuleHandler;
        this.deleteRuleHandler = deleteRuleHandler;
        this.takeTopicsHandler = takeTopicsHandler;
        this.takeLessonsHandler = takeLessonsHandler;
        this.createTopicHandler = createTopicHandler;
        this.deleteTopicHandler = deleteTopicHandler;
        this.takeCoursesHandler = takeCoursesHandler;
        this.createLessonHandler = createLessonHandler;
        this.deleteLessonHandler = deleteLessonHandler;
        this.takeChaptersHandler = takeChaptersHandler;
        this.createChapterHandler = createChapterHandler;
        this.deleteChapterHandler = deleteChapterHandler;
    }

    @GetMapping("/courses")
    public Mono<CoursesDto> takeAllCourses() {
        return injectPrincipalAndMono(takeCoursesHandler::handle);
    }

    @GetMapping("/courses/{id}/topics")
    public Mono<TopicsDto> takeAllTopics(@PathVariable(name = "id") String courseId) {
        final var command = new TakeTopicsCommand();
        command.setCourseId(courseId);
        return injectPrincipalAndThen(command, takeTopicsHandler::handle);
    }

    @PostMapping("/courses/{id}/topics")
    public Mono<Topic> createTopic(@PathVariable(name = "id") String courseId,
                                   @RequestBody @Valid CreateTopicCommand command) {
        command.setCourseId(courseId);
        return injectPrincipalAndThen(command, createTopicHandler::handle);
    }

    @DeleteMapping("/courses/{courseId}/topics/{topicId}")
    public Mono<TopicsDto> deleteTopic(@PathVariable String courseId,
                                  @PathVariable String topicId) {
        final var command = new DeleteTopicCommand();
        command.setCourseId(courseId);
        command.setTopicId(topicId);
        return injectPrincipalAndThen(command, deleteTopicHandler::handle);
    }

    @GetMapping("/topics/{id}/lessons")
    public Mono<LessonsDto> takeAllLessons(@PathVariable(name = "id") String topicId) {
        final var command = new TakeLessonsCommand();
        command.setTopicId(topicId);
        return injectPrincipalAndThen(command, takeLessonsHandler::handle);
    }

    @PostMapping("/topics/{id}/lessons")
    public Mono<Lesson> createLesson(@PathVariable(name = "id") String topicId,
                                     @RequestBody @Valid CreateLessonCommand command) {
        command.setTopicId(topicId);
        return injectPrincipalAndThen(command, createLessonHandler::handle);
    }

    @DeleteMapping("/topics/{topicId}/lessons/{lessonId}")
    public Mono<LessonsDto> deleteLesson(@PathVariable String topicId,
                                        @PathVariable String lessonId) {
        final var command = new DeleteLessonCommand();
        command.setTopicId(topicId);
        command.setLessonId(lessonId);
        return injectPrincipalAndThen(command, deleteLessonHandler::handle);
    }

    @GetMapping("/lessons/{id}/chapters")
    public Mono<ChaptersDto> takeAllChapters(@PathVariable(name = "id") String lessonId) {
        final var command = new TakeChaptersCommand();
        command.setLessonId(lessonId);
        return injectPrincipalAndThen(command, takeChaptersHandler::handle);
    }

    @PostMapping("/lessons/{id}/chapters")
    public Mono<Chapter> createChapter(@PathVariable(name = "id") String lessonId,
                                       @RequestBody @Valid CreateChapterCommand command) {
        command.setLessonId(lessonId);
        return injectPrincipalAndThen(command, createChapterHandler::handle);
    }

    @DeleteMapping("/lessons/{lessonId}/chapters/{chapterId}")
    public Mono<ChaptersDto> deleteChapter(@PathVariable String lessonId,
                                           @PathVariable String chapterId) {
        final var command = new DeleteChapterCommand();
        command.setLessonId(lessonId);
        command.setChapterId(chapterId);
        return injectPrincipalAndThen(command, deleteChapterHandler::handle);
    }

    @GetMapping("/chapters/{id}/rules")
    public Mono<RulesDto> takeAllRules(@PathVariable(name = "id") String chapterId) {
        final var command = new TakeRulesCommand();
        command.setChapterId(chapterId);
        return injectPrincipalAndThen(command, takeRulesHandler::handle);
    }

    @PostMapping("/chapters/{id}/rules")
    public Mono<CheckRule> createRule(@PathVariable(name = "id") String chapterId,
                                      @RequestBody @Valid CreateRuleCommand command) {
        command.setChapterId(chapterId);
        return injectPrincipalAndThen(command, createRuleHandler::handle);
    }

    @DeleteMapping("/chapters/{chapterId}/rules/{ruleId}")
    public Mono<RulesDto> deleteRule(@PathVariable String chapterId,
                                     @PathVariable String ruleId) {
        final var command = new DeleteRuleCommand();
        command.setChapterId(chapterId);
        command.setRuleId(ruleId);
        return injectPrincipalAndThen(command, deleteRuleHandler::handle);
    }

}

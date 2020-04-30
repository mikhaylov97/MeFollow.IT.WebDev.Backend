package com.mefollow.webschool.sandbox.usecase.TakeCourse;

import com.mefollow.webschool.sandbox.domain.base.Course;
import com.mefollow.webschool.sandbox.domain.dto.BriefCourseDto;
import com.mefollow.webschool.sandbox.domain.dto.FullCourseDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.CourseRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.LessonRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.ProgressRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeCourseHandler {

    private static final String LESSON_URL_TEMPLATE = "/course/%s/lesson/%s";

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final ProgressRepository progressRepository;

    public TakeCourseHandler(CourseRepository courseRepository,
                             LessonRepository lessonRepository,
                             ChapterRepository chapterRepository,
                             ProgressRepository progressRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.progressRepository = progressRepository;
    }

    public Mono<FullCourseDto> takeCourse(TakeCourseCommand command) {
        return courseRepository.findById(command.getCourseId())
                .switchIfEmpty(error(COURSE_NOT_FOUND))
                .flatMap(course -> progressRepository.findFirstByUserIdAndCourseId(command.getUserId(), course.getId())
                        .flatMap(progress -> chapterRepository.findById(progress.getChapterId())
                                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                                .map(chapter -> {
                                    final var continueUrl = format(LESSON_URL_TEMPLATE, progress.getCourseId(), chapter.getLessonId());
                                    return new FullCourseDto(course, continueUrl, true);
                                }))
                        .switchIfEmpty(constructFullCourseDtoWhenLearningIsNotStarted(course)));
    }

    private Mono<FullCourseDto> constructFullCourseDtoWhenLearningIsNotStarted(Course course) {
        return lessonRepository.findFirstByCourseIdOrderByOrderIndex(course.getId())
                .switchIfEmpty(error(LESSON_NOT_FOUND))
                .map(firstLesson -> {
                    final var continueUrl = format(LESSON_URL_TEMPLATE, course.getId(), firstLesson.getId());
                    return new FullCourseDto(course, continueUrl, false);
                });
    }

    public Flux<BriefCourseDto> takeAllCourses() {
        return courseRepository.findAll()
                .map(BriefCourseDto::new);
    }
}

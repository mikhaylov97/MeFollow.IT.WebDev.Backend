package com.mefollow.webschool.sandbox.usecase.MoveToNextChapter;

import com.mefollow.webschool.sandbox.domain.base.Chapter;
import com.mefollow.webschool.sandbox.domain.base.Lesson;
import com.mefollow.webschool.sandbox.domain.base.Progress;
import com.mefollow.webschool.sandbox.domain.base.Topic;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.LessonRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.ProgressRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.TopicRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.sandbox.domain.base.SandboxException.*;
import static java.util.Comparator.comparing;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.error;

@Service
public class MoveToNextChapterHandler {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final ProgressRepository progressRepository;

    public MoveToNextChapterHandler(TopicRepository topicRepository,
                                    LessonRepository lessonRepository,
                                    ChapterRepository chapterRepository,
                                    ProgressRepository progressRepository) {
        this.topicRepository = topicRepository;
        this.lessonRepository = lessonRepository;
        this.chapterRepository = chapterRepository;
        this.progressRepository = progressRepository;
    }

    public Mono<Void> handle(MoveToNextChapterCommand command) {
        return progressRepository.findFirstByUserIdAndCourseId(command.getUserId(), command.getCourseId())
                .switchIfEmpty(error(PROGRESS_NOT_FOUND))
                .flatMap(progress -> progress.isSolved()
                        ? processHandling(progress)
                        : error(CHAPTER_IS_NOT_SOLVED));
    }

    private Mono<Void> processHandling(Progress progress) {
        return chapterRepository.findById(progress.getChapterId())
                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                .flatMap(chapter -> chapterRepository.findAllByLessonIdOrderByOrderIndex(chapter.getLessonId())
                        .collectList()
                        .flatMap(chapters -> {
                            final var lastChapter = chapters.get(chapters.size() - 1);
                            if (!lastChapter.getId().equals(chapter.getId())) {
                                final var nextChapter = chapters.stream().filter(c -> c.getOrderIndex() > chapter.getOrderIndex()).min(comparing(Chapter::getOrderIndex));
                                if (nextChapter.isEmpty()) return error(CHAPTER_NOT_FOUND);
                                progress.setChapterId(nextChapter.get().getId());
                                progress.setSolved(false);
                                return progressRepository.save(progress).then();
                            }

                            return processHandlingForNextLesson(progress, chapter);
                        }));
    }

    private Mono<Void> processHandlingForNextLesson(Progress progress, Chapter chapter) {
        return lessonRepository.findById(chapter.getLessonId())
                .switchIfEmpty(error(LESSON_NOT_FOUND))
                .flatMap(lesson -> lessonRepository.findAllByTopicIdOrderByOrderIndex(lesson.getTopicId())
                        .collectList()
                        .flatMap(lessons -> {
                            final var lastLesson = lessons.get(lessons.size() - 1);
                            if (!lastLesson.getId().equals(lesson.getId())) {
                                final var nextLesson = lessons.stream().filter(l -> l.getOrderIndex() > lesson.getOrderIndex()).min(comparing(Lesson::getOrderIndex));
                                if (nextLesson.isEmpty()) return error(LESSON_NOT_FOUND);
                                return chapterRepository.findFirstByLessonIdOrderByOrderIndex(nextLesson.get().getId())
                                        .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                                        .flatMap(nextChapter -> {
                                            progress.setChapterId(nextChapter.getId());
                                            progress.setSolved(false);
                                            return progressRepository.save(progress).then();
                                        });
                            }

                            return processHandlingForNextTopic(progress, lesson);
                        }));
    }

    private Mono<Void> processHandlingForNextTopic(Progress progress, Lesson lesson) {
        return topicRepository.findById(lesson.getTopicId())
                .switchIfEmpty(error(TOPIC_NOT_FOUND))
                .flatMap(topic -> topicRepository.findAllByCourseIdOrderByOrderIndex(topic.getCourseId())
                        .collectList()
                        .flatMap(topics -> {
                            final var lastTopic = topics.get(topics.size() - 1);
                            if (!lastTopic.getId().equals(topic.getId())) {
                                final var nextTopic = topics.stream().filter(t -> t.getOrderIndex() > topic.getOrderIndex()).min(comparing(Topic::getOrderIndex));
                                if (nextTopic.isEmpty()) return error(TOPIC_NOT_FOUND);
                                return lessonRepository.findFirstByTopicIdOrderByOrderIndex(nextTopic.get().getId())
                                        .switchIfEmpty(error(LESSON_NOT_FOUND))
                                        .flatMap(nextLesson -> chapterRepository.findFirstByLessonIdOrderByOrderIndex(nextLesson.getId())
                                                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                                                .flatMap(nextChapter -> {
                                                    progress.setChapterId(nextChapter.getId());
                                                    progress.setSolved(false);
                                                    return progressRepository.save(progress).then();
                                                }));
                            }

                            return empty().then();
                        }));
    }
}

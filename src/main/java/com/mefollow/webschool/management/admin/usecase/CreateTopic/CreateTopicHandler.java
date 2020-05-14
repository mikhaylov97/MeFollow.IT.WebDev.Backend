package com.mefollow.webschool.management.admin.usecase.CreateTopic;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.sandbox.domain.base.Topic;
import com.mefollow.webschool.sandbox.infrastructure.repository.TopicRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.ManagementException.TOPIC_ALREADY_EXISTS;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;

@Service
public class CreateTopicHandler {

    private final TopicRepository topicRepository;
    private final PermissionService permissionService;

    public CreateTopicHandler(TopicRepository topicRepository,
                              PermissionService permissionService) {
        this.topicRepository = topicRepository;
        this.permissionService = permissionService;
    }

    public Mono<Topic> handle(CreateTopicCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<Topic> processHandling(CreateTopicCommand command) {
        return topicRepository.existsTopicByCourseIdAndOrderIndex(command.getCourseId(), command.getOrderIndex())
                .flatMap(exists -> !exists
                        ? topicRepository.save(new Topic(command.getCourseId(), command.getTitle(), command.getOrderIndex()))
                        : error(TOPIC_ALREADY_EXISTS));
    }
}

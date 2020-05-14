package com.mefollow.webschool.management.admin.usecase.TakeTopics;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.TopicsDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.TopicRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeTopicsHandler {

    private final TopicRepository topicRepository;
    private final PermissionService permissionService;

    public TakeTopicsHandler(TopicRepository topicRepository,
                             PermissionService permissionService) {
        this.topicRepository = topicRepository;
        this.permissionService = permissionService;
    }

    public Mono<TopicsDto> handle(TakeTopicsCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<TopicsDto> processHandling(TakeTopicsCommand command) {
        return topicRepository.findAllByCourseIdOrderByOrderIndex(command.getCourseId())
                .collectList()
                .map(topics -> {
                    final var dto = new TopicsDto();
                    dto.setMinAvailableOrderIndex(topics.size() + 1);
                    topics.forEach(topic -> dto.addTopic(topic.getId(), topic.getTitle(), topic.getOrderIndex()));

                    return dto;
                });
    }
}

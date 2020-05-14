package com.mefollow.webschool.management.admin.usecase.DeleteTopic;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.TopicsDto;
import com.mefollow.webschool.sandbox.infrastructure.repository.TopicRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class DeleteTopicHandler {

    private final TopicRepository topicRepository;
    private final PermissionService permissionService;

    public DeleteTopicHandler(TopicRepository topicRepository, PermissionService permissionService) {
        this.topicRepository = topicRepository;
        this.permissionService = permissionService;
    }

    public Mono<TopicsDto> handle(DeleteTopicCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<TopicsDto> processHandling(DeleteTopicCommand command) {
        final var dto = new TopicsDto();
        return topicRepository.findAllByCourseIdOrderByOrderIndex(command.getCourseId())
                .collectList()
                .map(topics -> {
                    topics.removeIf(topic -> topic.getId().equals(command.getTopicId()));
                    dto.setMinAvailableOrderIndex(topics.size() + 1);

                    for (int i = 0; i < topics.size(); i++) {
                        topics.get(i).setOrderIndex(i + 1);
                        dto.addTopic(topics.get(i).getId(), topics.get(i).getTitle(), topics.get(i).getOrderIndex());
                    }

                    return topics;
                })
                .flatMapMany(topicRepository::saveAll)
                .then(topicRepository.deleteById(command.getTopicId()))
                .then(just(dto));
    }
}

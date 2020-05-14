package com.mefollow.webschool.management.admin.usecase.TakeCourses;

import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.CoursesDto;
import com.mefollow.webschool.management.user.domain.account.User;
import com.mefollow.webschool.sandbox.infrastructure.repository.CourseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeCoursesHandler {

    private final CourseRepository courseRepository;
    private final PermissionService permissionService;

    public TakeCoursesHandler(CourseRepository courseRepository,
                              PermissionService permissionService) {
        this.courseRepository = courseRepository;
        this.permissionService = permissionService;
    }

    public Mono<CoursesDto> handle(User user) {
        return permissionService.checkPermission(user.getId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling()
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<CoursesDto> processHandling() {
        return courseRepository.findAll()
                .collectList()
                .map(courses -> {
                    final var dto = new CoursesDto();
                    courses.forEach(course -> dto.addCourse(course.getId(), course.getTitle()));

                    return dto;
                });
    }
}

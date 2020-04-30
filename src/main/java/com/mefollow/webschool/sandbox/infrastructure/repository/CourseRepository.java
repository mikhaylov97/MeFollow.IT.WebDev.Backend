package com.mefollow.webschool.sandbox.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.sandbox.domain.base.Course;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends AbstractRepository<Course> {
}

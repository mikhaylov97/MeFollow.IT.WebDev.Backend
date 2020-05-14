package com.mefollow.webschool.management.admin.infrastructure.repository;

import com.mefollow.webschool.core.repository.AbstractRepository;
import com.mefollow.webschool.management.admin.domain.base.Permission;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PermissionRepository extends AbstractRepository<Permission> {
    Mono<Permission> findFirstByUserId(String userId);
}

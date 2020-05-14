package com.mefollow.webschool.management.admin.application;

import com.mefollow.webschool.management.admin.domain.base.PermissionLevel;
import com.mefollow.webschool.management.admin.infrastructure.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Mono<Boolean> checkPermission(String userId, PermissionLevel permissionLevel) {
        return permissionRepository.findFirstByUserId(userId)
                .map(permission -> permission.getPermissionLevel().isUpperOrEquals(permissionLevel))
                .defaultIfEmpty(false);
    }
}

package com.its.auth_service.repository;

import com.its.auth_service.model.entity.RolePermissionEntity;
import com.its.module.utils.Constants.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Integer> {
    @CacheEvict(value = CacheName.ROLE_PERMISSION, allEntries = true)
    RolePermissionEntity save(RolePermissionEntity rolePermissionEntity);

    RolePermissionEntity findByRoleIdAndPermissionIdAndIsActiveIsTrue(Integer roleId, Integer permissionId);
}

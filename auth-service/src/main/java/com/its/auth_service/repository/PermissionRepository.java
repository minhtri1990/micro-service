package com.its.auth_service.repository;

import com.its.auth_service.model.entity.PermissionEntity;
import com.its.module.utils.Constants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Integer> {
    @Cacheable(value = Constants.CacheName.ROLE_PERMISSION, key = "#role", condition = "#role != null")
    @Query(value = "select permission_id from its.role_permission where role_id=:role and is_active=1;", nativeQuery = true)
    Set<Integer> findIdsByRole(Integer role);
}


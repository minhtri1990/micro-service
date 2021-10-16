package com.its.auth_service.repository;

import com.its.auth_service.model.entity.RoleEntity;
import com.its.auth_service.model.entity.RolePermissionEntity;
import com.its.module.utils.Constants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    List<RoleEntity> findByIsActiveIsTrue();
    RoleEntity findByIdAndIsActiveIsTrue(Integer id);
    @CacheEvict(value = Constants.CacheName.ROLE_PERMISSION, allEntries = true)
    RoleEntity save(RoleEntity roleEntity);
}

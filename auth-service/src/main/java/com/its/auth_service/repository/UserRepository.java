package com.its.auth_service.repository;

import java.util.List;

import com.its.auth_service.model.dto.UserSearchDto;
import com.its.module.utils.Constants.*;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.its.auth_service.model.dto.UserPaginationDto;
import com.its.auth_service.model.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = "select * from user where sso_id=:ssoId and is_active=1;", nativeQuery = true)
    @Cacheable(value = CacheName.USER, key = "#ssoId")
    UserEntity findBySsoId(String ssoId);

    @Query(value = "select * from user where lower(email)=lower(:email) and is_active=1 limit 1;", nativeQuery = true)
    UserEntity findUserByEmail(String email);

    @Query(value = "select count(*) from its.user where is_active=1 and (username=:username or email=:email)", nativeQuery = true)
    Integer countByUsernameOrEmail(String username, String email);

    @Query(value = "select * from its.user where is_active=1 and (username=:username or email=:email)", nativeQuery = true)
    UserEntity findByUsernameOrEmail(String username, String email);

    @CachePut(value = CacheName.USER, key = "#result.ssoId", condition = "#result != null")
    UserEntity findByIdAndIsActiveIsTrue(Integer userId);

    @Query(value = "CALL proc_getAllUsers(:offset, :limit);", nativeQuery = true)
    List<UserEntity> getAllPagination(Integer offset, Integer limit);

    @Query(value = "select * from user where is_active=1 order by username asc", nativeQuery = true)
    List<UserEntity> getAllUsers();

    @Query(value = "CALL its.proc_searchUsers(?,?,?,?,?,?);", nativeQuery = true)
    List<UserEntity> searchUsers(String username, String email, Integer role, Boolean isActive, Integer limit, Integer offset);

    @CachePut(value = CacheName.USER, key = "#result.ssoId", condition = "#result != null")
    UserEntity save(UserEntity userEntity);
}

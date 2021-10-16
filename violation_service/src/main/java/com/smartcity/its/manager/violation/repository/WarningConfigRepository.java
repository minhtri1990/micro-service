package com.smartcity.its.manager.violation.repository;

import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningConfigRepository extends JpaRepository<WarningConfigEntity, Integer> {
    @Query(value = "SELECT count(*) FROM user WHERE user_id=:userId", nativeQuery = true)
    Integer checkValidCreatingId(Integer userId);

    @Query(value = "SELECT * FROM warning_config w WHERE w.warning_config_id=? AND w.type=?;", nativeQuery = true)
    WarningConfigEntity getByUserIdAndTypeNative(Integer userId, Integer type);

    List<WarningConfigEntity> findByType(Integer type);
  
    @Query(value = "SELECT DISTINCT(warning_config_id) FROM warning_config;", nativeQuery = true)
    List<Integer> getAllSupervisorIds();
}

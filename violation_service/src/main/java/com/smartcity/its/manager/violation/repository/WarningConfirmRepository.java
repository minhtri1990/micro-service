package com.smartcity.its.manager.violation.repository;

import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfirmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WarningConfirmRepository extends JpaRepository<WarningConfirmEntity, Long> {
    @Query(value = "SELECT MAX(created_date) FROM warning_confirm WHERE user_id=:userId", nativeQuery = true)
    LocalDateTime getLastByUser(Integer userId);

    @Query(value = "select func_getMaxConfirmTimeByDeviceId(?);", nativeQuery = true)
    LocalDateTime getLastByDeviceId(String deviceId);
}

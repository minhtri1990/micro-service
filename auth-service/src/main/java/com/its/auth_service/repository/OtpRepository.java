package com.its.auth_service.repository;

import com.its.auth_service.model.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Integer> {
    @Query(value = "select * from otp where created_date < :time ;", nativeQuery = true)
    List<OtpEntity> getExpiredOtp(String time);

    @Query(value = "select * from otp where email=:email and type=:type order by created_date desc limit 1;", nativeQuery = true)
    OtpEntity getLastByEmail(String email, Integer type);
}

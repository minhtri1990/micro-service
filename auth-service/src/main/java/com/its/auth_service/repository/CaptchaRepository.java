package com.its.auth_service.repository;

import com.its.auth_service.model.entity.CaptchaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaEntity, Integer> {
    @Query(value = "select * from captcha where created_date < :time ;", nativeQuery = true)
    List<CaptchaEntity> getExpiredCaptcha(String time);

    @Query(value = "CALL proc_getValidCaptcha(:validateCode, :captchaCode, :expSecond);", nativeQuery = true)
    Integer checkCaptchaValidation(String validateCode, String captchaCode, Integer expSecond);

    CaptchaEntity findByValidateCodeAndIsActiveIsTrue(String validateCode);
}

package com.its.auth_service.component;

import com.its.auth_service.model.entity.CaptchaEntity;
import com.its.auth_service.model.entity.OtpEntity;
import com.its.auth_service.repository.CaptchaRepository;
import com.its.auth_service.repository.OtpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CleaningTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CleaningTask.class);

    @Autowired
    private CaptchaRepository captchaRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Value("${cleaner.captcha.exp}")
    private Integer CAPTCHA_EXP;

    @Value("${cleaner.otp.exp}")
    private Integer OTP_EXP;

    @Scheduled(fixedDelay = 3600000, initialDelay = 0)
    public void cleanCaptcha() {
        LocalDateTime time = LocalDateTime.now().minusSeconds(CAPTCHA_EXP);
        List<CaptchaEntity> captchaEntities = captchaRepository.getExpiredCaptcha(time.toString());
        new Thread(() -> {
            int cnt = 0;
            for(int i=0; i<captchaEntities.size(); i++) {
                try {
                    captchaRepository.delete(captchaEntities.get(i));
                    cnt++;
                } catch (Exception e) {
                    LOGGER.info("captcha", e);
                }
                if(cnt%10 == 0) LOGGER.info(String.format("Delete %s/%s captcha - %s failed", cnt, captchaEntities.size(), i-cnt));
            }
            LOGGER.info(String.format("Cleaned %s captcha", cnt));
        }).start();
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 0)
    public void cleanOtp() {
        LocalDateTime time = LocalDateTime.now().minusSeconds(OTP_EXP);
        List<OtpEntity> otpEntities = otpRepository.getExpiredOtp(time.toString());
        new Thread(() -> {
            int cnt = 0;
            for(int i=0; i<otpEntities.size(); i++) {
                try {
                    otpRepository.delete(otpEntities.get(i));
                    cnt++;
                } catch (Exception e) {
                    LOGGER.error("khong the xoa captcha", e);
                }
                if(cnt%10 == 0) LOGGER.info(String.format("Delete %s/%s otp - %s failed", cnt, otpEntities.size(), i-cnt));
            }
            LOGGER.info(String.format("Cleaned %s otp", cnt));
        }).start();
    }
}

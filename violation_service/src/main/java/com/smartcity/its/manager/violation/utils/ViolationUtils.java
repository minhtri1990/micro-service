package com.smartcity.its.manager.violation.utils;

import com.its.module.model.exception.BadRequestException;
import com.smartcity.its.manager.violation.configuration.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ValidationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class ViolationUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtils.class);
    public enum SendingType {
        NO_WARNING,
        IMMEDIATE,
        DAILY
    }

    public enum SendingMethod {
        EMAIL,
        SMS,
        NOTIFICATION,
        AUTO_CALLING
    }


    public enum Type {
        VIOLATION,
        ABNORMAL,
        ACCIDENT
    }

    public interface SearchType {
        String TYPE_ID = "typeId";
        String DEVICE_ID = "deviceId";
        String VIOLATION_ID = "violationId";
        String UNREAD = "unread";
        String STATUS = "status";
        String IS_VIOLATION = "is_violation";
    }

    public static final Long VIOLATION_PROOF_LENGTH = 10000L;
    /**
     * chuyen doi string thanh day cac so
     * kiem tra tinh hop le
     *
     * @param s
     * @return
     */
    public static List<Integer> convertStringToList(String s) {
        if(s.length() == 0) return new ArrayList<>();
        List<String> methods = Arrays.asList(s.split(";"));
        List<Integer> result = new ArrayList<>();
        for (String method : methods) {
            result.add(Integer.valueOf(method));
        }
        return result;
    }

    //TODO: translate

    /**
     * chuyen doi list cac sending method sang string phan tach boi dau ;
     * loai bo cac phan tu trung nhau
     *
     * @param list
     * @return
     */
    public static String convertListToString(List<Integer> list) {
        if (list.size() == 0) return "";
        Set<Integer> set = new HashSet<>(list);
        if (set.size() < list.size())
            throw new BadRequestException(WebConfig.translate("warning_config_item.duplicate_sending_method"));
        boolean first = true;
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer item : set) {
            if (first) first = false;
            else
                stringBuilder.append(";");

            if (item < 0 || item >= SendingMethod.values().length)
                throw new BadRequestException(WebConfig.translate("warning_config_item.not_valid_sending_method"));
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }

    public static String getStringFromStream(InputStream inputStream) throws IOException {
        InputStreamReader is = new InputStreamReader(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[1024];
        int len;
        while((len = is.read(buffer)) > 0) {
            stringBuilder.append(buffer, 0, len);
        }
        is.close();
        return stringBuilder.toString();
    }
}

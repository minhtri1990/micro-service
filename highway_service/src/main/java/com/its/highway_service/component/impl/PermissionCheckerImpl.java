package com.its.highway_service.component.impl;

import com.its.highway_service.component.PermissionChecker;
import org.springframework.stereotype.Component;

public class PermissionCheckerImpl implements PermissionChecker {
    @Override
    public boolean checkId(Integer id1, Integer id2) {
        System.out.println(id1 + " " + id2);
        return id1 == id2;
    }
}

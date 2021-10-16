package com.its.module.model.entity;

import java.time.LocalDateTime;

public interface Creatable {
    void setCreatedDate(LocalDateTime time);
    void setCreatedBy(Integer authId);
}

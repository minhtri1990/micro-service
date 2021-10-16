package com.its.module.model.entity;

import java.time.LocalDateTime;

public interface Modifiable {
    void setModifiedDate(LocalDateTime time);
    void setModifiedBy(Integer authId);
}

package com.its.module.model.entity;

public interface Editable extends Creatable, Modifiable {
    void setIsActive(Boolean isActive);
}

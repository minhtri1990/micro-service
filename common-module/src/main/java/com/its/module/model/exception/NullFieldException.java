package com.its.module.model.exception;

public class NullFieldException extends FieldException {
    public NullFieldException(String field) {
        super(field, "field.null");
    }
}

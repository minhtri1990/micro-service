package com.its.module.model.exception;

public class NotvalidFieldException extends FieldException {
    public NotvalidFieldException(String field) {
        super(field, "field.notvalid");
    }
}

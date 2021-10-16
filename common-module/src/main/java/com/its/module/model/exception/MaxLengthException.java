package com.its.module.model.exception;

public class MaxLengthException extends FieldException {
    public MaxLengthException(String field) {
        super(field, "field.maxlength");
    }
}

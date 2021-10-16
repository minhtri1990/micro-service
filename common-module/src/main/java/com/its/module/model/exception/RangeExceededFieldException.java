package com.its.module.model.exception;

public class RangeExceededFieldException extends FieldException {
    public RangeExceededFieldException(String field) {
        super(field, "field.range_exceed");
    }
}

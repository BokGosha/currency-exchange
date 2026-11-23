package com.currencyexchange.validator;

import com.currencyexchange.exception.ValidationException;

public abstract class BaseValidator {

    protected void validateBlank(String fieldName, String value) {
        if (value.isBlank()) {
            throw new ValidationException("Аргумент %s пустой".formatted(fieldName));
        }
    }

    protected void validateNull(String fieldName, String value) {
        if (value == null) {
            throw new ValidationException("Аргумент %s отсутствует".formatted(fieldName));
        }
    }
}

package com.currencyexchange.filter;

import lombok.Getter;

@Getter
public enum MediaType {

    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JSON("application/json");

    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public boolean isMediaType(String type) {
        return value.equalsIgnoreCase(type);
    }
}

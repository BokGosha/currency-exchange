package com.currencyexchange.util;

import com.currencyexchange.exception.ValidationException;

public final class UriParser {

    private static final int REQUIRED_PATH_PARTS_COUNT = 2;

    private UriParser() {
    }

    public static String parse(String uri) {
        if (uri == null) {
            throw new ValidationException("Path‑параметр не указан");
        }

        String[] pathParts = uri.split("/");
        if (pathParts.length != REQUIRED_PATH_PARTS_COUNT) {
            throw new ValidationException("Неверное количество path‑параметров");
        }

        return pathParts[1];
    }
}

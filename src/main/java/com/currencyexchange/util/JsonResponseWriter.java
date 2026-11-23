package com.currencyexchange.util;

import com.currencyexchange.dto.ErrorResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class JsonResponseWriter {

    private static final Gson GSON = new Gson();

    private JsonResponseWriter() {
    }

    public static void writeOk(HttpServletResponse response, Object body) throws IOException {
        write(response, HttpServletResponse.SC_OK, body);
    }

    public static void writeCreated(HttpServletResponse response, Object body) throws IOException {
        write(response, HttpServletResponse.SC_CREATED, body);
    }

    public static void writeError(HttpServletResponse response, int status, ErrorResponse errorResponse) throws IOException {
        write(response, status, errorResponse);
    }

    private static void write(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.getWriter().write(GSON.toJson(body));
    }
}

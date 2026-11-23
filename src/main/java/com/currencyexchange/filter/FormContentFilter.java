package com.currencyexchange.filter;

import com.currencyexchange.exception.IncorrectContentTypeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter(urlPatterns = "/*")
public class FormContentFilter extends HttpFilter {

    private static final String METHOD_WITHOUT_PARSED_BODY = "PATCH";

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String method = request.getMethod();
        if (!method.equals(METHOD_WITHOUT_PARSED_BODY)) {
            chain.doFilter(request, response);
            return;
        }

        String contentTypeRaw = request.getContentType();
        if (contentTypeRaw == null) {
            throw new ServletException("Пустой content-type");
        }

        String contentType = contentTypeRaw.split(";")[0].strip();
        if (!MediaType.APPLICATION_FORM_URLENCODED.isMediaType(contentType)) {
            chain.doFilter(request, response);
            return;
        }

        FormContentRequestWrapper requestWrapper = new FormContentRequestWrapper(request, parseBody(request));

        chain.doFilter(requestWrapper, response);
    }

    private Map<String, String[]> parseBody(HttpServletRequest request) throws IOException {
        Map<String, List<String>>  params = new HashMap<>();
        Map<String, String[]> result = new HashMap<>();

        String body = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        if (body.isBlank()) {
            return result;
        }

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8) : "";

            params.computeIfAbsent(key, p -> new ArrayList<>()).add(value);
        }

        for (Map.Entry<String, List<String>> param : params.entrySet()) {
            result.put(param.getKey(), param.getValue().toArray(new String[0]));
        }

        return result;
    }
}

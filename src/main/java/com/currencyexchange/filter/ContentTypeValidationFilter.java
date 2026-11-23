package com.currencyexchange.filter;

import com.currencyexchange.exception.IncorrectContentTypeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(
        urlPatterns = {
                "/currencies",
                "/exchangeRates",
                "/exchangeRate/*"
        }
)
public class ContentTypeValidationFilter extends HttpFilter {

    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PATCH");

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String method = request.getMethod();
        if (!METHODS_WITH_BODY.contains(method)) {
            chain.doFilter(request, response);
            return;
        }

        String contentTypeRaw = request.getContentType();
        if (contentTypeRaw == null) {
            throw new ServletException("Пустой content-type");
        }

        String contentType = contentTypeRaw.split(";")[0].strip();
        if (!MediaType.APPLICATION_FORM_URLENCODED.isMediaType(contentType)) {
            throw new IncorrectContentTypeException("Неверный content-type, ожидался %s, получен %s".formatted(
                    MediaType.APPLICATION_FORM_URLENCODED.getValue(),
                    contentType)
            );
        }

        chain.doFilter(request, response);
    }
}

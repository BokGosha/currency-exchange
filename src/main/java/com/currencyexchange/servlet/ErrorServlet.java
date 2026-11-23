package com.currencyexchange.servlet;

import com.currencyexchange.dto.ErrorResponse;
import com.currencyexchange.exception.ExchangeRateNotFoundException;
import com.currencyexchange.util.JsonResponseWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error-handler")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (throwable == null) {
            JsonResponseWriter.writeError(response, HttpServletResponse.SC_NOT_FOUND, new ErrorResponse("Эндпоинт не найден"));
            return;
        }

        switch (throwable.getClass().getSimpleName()) {
            case "ExchangeRateNotFoundException", "CurrencyNotFoundException" -> JsonResponseWriter.writeError(response,
                    HttpServletResponse.SC_NOT_FOUND, new ErrorResponse(throwable.getMessage()));
            case "ExchangeRateAlreadyExistsException", "CurrencyAlreadyExistsException"
                    -> JsonResponseWriter.writeError(response, HttpServletResponse.SC_CONFLICT,
                    new ErrorResponse(throwable.getMessage()));
            case "IncorrectContentTypeException", "IllegalStateException", "ValidationException" ->
                    JsonResponseWriter.writeError(response, HttpServletResponse.SC_BAD_REQUEST,
                            new ErrorResponse(throwable.getMessage()));
            case "DataAccessException" -> JsonResponseWriter.writeError(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse(throwable.getMessage()));
            default -> JsonResponseWriter.writeError(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponse("Произошла непредвиденная ошибка"));
        }
    }
}

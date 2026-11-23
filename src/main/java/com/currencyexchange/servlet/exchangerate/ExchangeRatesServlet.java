package com.currencyexchange.servlet.exchangerate;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.listener.AppContextAttributes;
import com.currencyexchange.service.exchangerate.ExchangeRateService;
import com.currencyexchange.util.JsonResponseWriter;
import com.currencyexchange.validator.ExchangeRateValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private ExchangeRateService exchangeRateService;
    private ExchangeRateValidator exchangeRateValidator;

    @Override
    public void init() {
        exchangeRateService = (ExchangeRateService) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_RATE_SERVICE);
        exchangeRateValidator = (ExchangeRateValidator) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_RATE_VALIDATOR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonResponseWriter.writeOk(response, exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        CurrencyCodePair currencyCodePair = new CurrencyCodePair(baseCurrencyCode, targetCurrencyCode);

        ExchangeRateRequestDto newExchangeRate = new ExchangeRateRequestDto(currencyCodePair, rate);

        exchangeRateValidator.validate(newExchangeRate);

        JsonResponseWriter.writeCreated(response, exchangeRateService.create(newExchangeRate));
    }
}

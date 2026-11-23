package com.currencyexchange.servlet.exchangerate;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.listener.AppContextAttributes;
import com.currencyexchange.service.exchangerate.ExchangeRateService;
import com.currencyexchange.util.JsonResponseWriter;
import com.currencyexchange.util.CurrencyCodePairParser;
import com.currencyexchange.util.UriParser;
import com.currencyexchange.validator.CurrencyValidator;
import com.currencyexchange.validator.ExchangeRateValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private ExchangeRateService exchangeRateService;
    private ExchangeRateValidator exchangeRateValidator;
    private CurrencyValidator currencyValidator;

    @Override
    public void init() {
        exchangeRateService = (ExchangeRateService) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_RATE_SERVICE);
        exchangeRateValidator = (ExchangeRateValidator) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_RATE_VALIDATOR);
        currencyValidator = (CurrencyValidator) getServletContext()
                .getAttribute(AppContextAttributes.CURRENCY_VALIDATOR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codePair = UriParser.parse(request.getPathInfo());

        CurrencyCodePair currencyCodePair = CurrencyCodePairParser.parse(codePair);

        currencyValidator.validateCode(currencyCodePair.baseCurrencyCode());
        currencyValidator.validateCode(currencyCodePair.targetCurrencyCode());

        JsonResponseWriter.writeOk(response, exchangeRateService.getByCodePair(currencyCodePair));
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codePair = UriParser.parse(request.getPathInfo());

        CurrencyCodePair currencyCodePair = CurrencyCodePairParser.parse(codePair);

        String rate = request.getParameter("rate");

        System.out.println(rate);

        ExchangeRateRequestDto updatedExchange = new ExchangeRateRequestDto(currencyCodePair, rate);

        exchangeRateValidator.validate(updatedExchange);

        JsonResponseWriter.writeOk(response, exchangeRateService.update(updatedExchange));
    }
}

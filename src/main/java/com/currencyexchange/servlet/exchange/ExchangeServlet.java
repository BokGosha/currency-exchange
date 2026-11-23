package com.currencyexchange.servlet.exchange;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchange.ExchangeRequestDto;
import com.currencyexchange.listener.AppContextAttributes;
import com.currencyexchange.service.exchange.ExchangeService;
import com.currencyexchange.util.JsonResponseWriter;
import com.currencyexchange.validator.ExchangeValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private ExchangeService exchangeService;
    private ExchangeValidator exchangeValidator;

    @Override
    public void init() {
        exchangeService = (ExchangeService) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_SERVICE);
        exchangeValidator = (ExchangeValidator) getServletContext()
                .getAttribute(AppContextAttributes.EXCHANGE_VALIDATOR);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amount = request.getParameter("amount");

        CurrencyCodePair currencyCodePair = new CurrencyCodePair(baseCurrencyCode, targetCurrencyCode);

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(currencyCodePair, amount);

        exchangeValidator.validate(exchangeRequestDto);

        JsonResponseWriter.writeOk(response, exchangeService.getExchange(exchangeRequestDto));
    }
}

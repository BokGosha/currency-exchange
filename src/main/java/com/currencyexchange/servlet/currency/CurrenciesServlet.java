package com.currencyexchange.servlet.currency;

import com.currencyexchange.dto.currency.CurrencyRequestDto;
import com.currencyexchange.listener.AppContextAttributes;
import com.currencyexchange.service.currency.CurrencyService;
import com.currencyexchange.util.JsonResponseWriter;
import com.currencyexchange.validator.CurrencyValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private CurrencyService currencyService;
    private CurrencyValidator currencyValidator;

    @Override
    public void init() {
        currencyService = (CurrencyService) getServletContext()
                .getAttribute(AppContextAttributes.CURRENCY_SERVICE);
        currencyValidator = (CurrencyValidator) getServletContext()
                .getAttribute(AppContextAttributes.CURRENCY_VALIDATOR);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonResponseWriter.writeOk(response, currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String fullName = request.getParameter("name");
        String sign = request.getParameter("sign");

        CurrencyRequestDto newCurrency = new CurrencyRequestDto(code, fullName, sign);

        currencyValidator.validate(newCurrency);

        JsonResponseWriter.writeCreated(response, currencyService.create(newCurrency));
    }
}

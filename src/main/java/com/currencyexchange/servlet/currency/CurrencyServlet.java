package com.currencyexchange.servlet.currency;

import com.currencyexchange.util.JsonResponseWriter;
import com.currencyexchange.util.UriParser;
import com.currencyexchange.listener.AppContextAttributes;
import com.currencyexchange.service.currency.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = (CurrencyService) getServletContext().getAttribute(AppContextAttributes.CURRENCY_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = UriParser.parse(request.getPathInfo());

        JsonResponseWriter.writeOk(response, currencyService.getByCode(code));
    }
}

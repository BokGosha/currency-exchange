package com.currencyexchange.listener;

import com.currencyexchange.dao.currency.CurrencyDao;
import com.currencyexchange.dao.exchangerate.ExchangeRateDao;
import com.currencyexchange.dao.currency.impl.CurrencyDaoImpl;
import com.currencyexchange.dao.exchangerate.impl.ExchangeRateDaoImpl;
import com.currencyexchange.db.HikariDataSourceFactory;
import com.currencyexchange.service.currency.CurrencyService;
import com.currencyexchange.service.exchangerate.ExchangeRateService;
import com.currencyexchange.service.exchange.ExchangeService;
import com.currencyexchange.service.currency.impl.CurrencyServiceImpl;
import com.currencyexchange.service.exchangerate.impl.ExchangeRateServiceImpl;
import com.currencyexchange.service.exchange.impl.ExchangeServiceImpl;
import com.currencyexchange.validator.CurrencyValidator;
import com.currencyexchange.validator.ExchangeRateValidator;
import com.currencyexchange.validator.ExchangeValidator;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        HikariDataSourceFactory.create();

        ServletContext context = event.getServletContext();

        CurrencyValidator currencyValidator = new CurrencyValidator();
        context.setAttribute(AppContextAttributes.CURRENCY_VALIDATOR, currencyValidator);
        CurrencyDao currencyDao = new CurrencyDaoImpl();
        CurrencyService currencyService = new CurrencyServiceImpl(currencyDao);
        context.setAttribute(AppContextAttributes.CURRENCY_SERVICE, currencyService);

        ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidator(currencyValidator);
        context.setAttribute(AppContextAttributes.EXCHANGE_RATE_VALIDATOR, exchangeRateValidator);
        ExchangeRateDao exchangeRateDao = new ExchangeRateDaoImpl();
        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl(exchangeRateDao, currencyService);
        context.setAttribute(AppContextAttributes.EXCHANGE_RATE_SERVICE, exchangeRateService);

        ExchangeValidator exchangeValidator = new ExchangeValidator(currencyValidator);
        context.setAttribute(AppContextAttributes.EXCHANGE_VALIDATOR, exchangeValidator);
        ExchangeService exchangeService = new ExchangeServiceImpl(exchangeRateDao, currencyService, exchangeRateService);
        context.setAttribute(AppContextAttributes.EXCHANGE_SERVICE, exchangeService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HikariDataSourceFactory.close();
    }
}

package com.currencyexchange.service.exchange.impl;

import com.currencyexchange.dao.exchangerate.ExchangeRateDao;
import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchange.ExchangeRequestDto;
import com.currencyexchange.dto.exchangerate.ExchangeRateResponseDto;
import com.currencyexchange.dto.exchange.ExchangeResponseDto;
import com.currencyexchange.exception.ExchangeRateNotFoundException;
import com.currencyexchange.mapper.ExchangeMapper;
import com.currencyexchange.model.Currency;
import com.currencyexchange.model.ExchangeRate;
import com.currencyexchange.service.currency.CurrencyService;
import com.currencyexchange.service.exchangerate.ExchangeRateService;
import com.currencyexchange.service.exchange.ExchangeService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private static final String USD_CODE = "USD";
    private static final int SCALE = 6;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private final ExchangeRateDao exchangeRateDao;

    private final CurrencyService currencyService;

    private final ExchangeRateService exchangeRateService;

    private final ExchangeMapper exchangeMapper = ExchangeMapper.INSTANCE;

    @Override
    public ExchangeResponseDto getExchange(ExchangeRequestDto exchangeRequestDto) {
        String baseCurrencyCode = exchangeRequestDto.currencyCodePair().baseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDto.currencyCodePair().targetCurrencyCode();

        Currency baseCurrency = currencyService.getEntityByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.getEntityByCode(targetCurrencyCode);

        CurrencyCodePair currencyCodePair = exchangeRequestDto.currencyCodePair();
        BigDecimal amount = new BigDecimal(exchangeRequestDto.amount());

        if (exchangeRateService.existsByCodePair(currencyCodePair)) {
            ExchangeRateResponseDto exchangeRate = exchangeRateService.getByCodePair(currencyCodePair);

            BigDecimal rate = exchangeRate.rate();
            BigDecimal convertedAmount = rate.multiply(amount);

            return exchangeMapper.toExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
        }

        if (exchangeRateService.existsByCodePair(currencyCodePair)) {
            ExchangeRateResponseDto exchangeRate = exchangeRateService.getByCodePair(currencyCodePair);

            BigDecimal rate = BigDecimal.ONE.divide(exchangeRate.rate(), SCALE, ROUNDING_MODE);
            BigDecimal convertedAmount = rate.multiply(amount);

            return exchangeMapper.toExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
        }

        Optional<ExchangeRate> base = exchangeRateDao.findByCodePair(
                new CurrencyCodePair(USD_CODE, baseCurrencyCode)
        );
        Optional<ExchangeRate> target = exchangeRateDao.findByCodePair(
                new CurrencyCodePair(USD_CODE, targetCurrencyCode)
        );
        if (base.isPresent() && target.isPresent()) {
            ExchangeRate exchangeRateBase = base.get();
            ExchangeRate exchangeRateTarget = target.get();

            BigDecimal rate = exchangeRateTarget.rate()
                    .divide(exchangeRateBase.rate(), SCALE, ROUNDING_MODE);
            BigDecimal convertedAmount = rate.multiply(amount);

            return exchangeMapper.toExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
        }

        throw new ExchangeRateNotFoundException("Невозможно обменять валюты с кодами %s и %s"
                .formatted(baseCurrencyCode, targetCurrencyCode));
    }
}

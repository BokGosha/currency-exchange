package com.currencyexchange.service.exchangerate.impl;

import com.currencyexchange.dao.exchangerate.ExchangeRateDao;
import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.dto.exchangerate.ExchangeRateResponseDto;
import com.currencyexchange.exception.ExchangeRateAlreadyExistsException;
import com.currencyexchange.exception.ExchangeRateNotFoundException;
import com.currencyexchange.mapper.ExchangeRateMapper;
import com.currencyexchange.model.Currency;
import com.currencyexchange.model.ExchangeRate;
import com.currencyexchange.service.currency.CurrencyService;
import com.currencyexchange.service.exchangerate.ExchangeRateService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao;

    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;

    private final CurrencyService currencyService;

    @Override
    public List<ExchangeRateResponseDto> findAll() {
        return exchangeRateMapper.toExchangeRateResponseDtoList(exchangeRateDao.findAll());
    }

    @Override
    public ExchangeRateResponseDto getByCodePair(CurrencyCodePair currencyCodePair) {
        ExchangeRate exchangeRate = exchangeRateDao.findByCodePair(currencyCodePair)
                .orElseThrow(() -> new ExchangeRateNotFoundException("Обменный курс валют с кодами %s и %s не найден"
                        .formatted(currencyCodePair.baseCurrencyCode(), currencyCodePair.targetCurrencyCode())));

        return exchangeRateMapper.toExchangeRateResponseDto(exchangeRate);
    }

    @Override
    public ExchangeRateResponseDto create(ExchangeRateRequestDto newExchangeRate) {
        String baseCurrencyCode = newExchangeRate.currencyCodePair().baseCurrencyCode();
        String targetCurrencyCode = newExchangeRate.currencyCodePair().targetCurrencyCode();

        if (existsByCodePair(newExchangeRate.currencyCodePair())) {
            throw new ExchangeRateAlreadyExistsException("Обменный курс валют с кодами %s и %s уже существует"
                    .formatted(baseCurrencyCode, targetCurrencyCode));
        }

        Currency baseCurrency = currencyService.getEntityByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.getEntityByCode(targetCurrencyCode);

        ExchangeRate exchangeRate = exchangeRateMapper.toExchangeRate(newExchangeRate, baseCurrency, targetCurrency);

        exchangeRate = exchangeRateDao.create(exchangeRate);

        return exchangeRateMapper.toExchangeRateResponseDto(exchangeRate);
    }

    @Override
    public ExchangeRateResponseDto update(ExchangeRateRequestDto updatedExchangeRate) {
        String baseCurrencyCode = updatedExchangeRate.currencyCodePair().baseCurrencyCode();
        String targetCurrencyCode = updatedExchangeRate.currencyCodePair().targetCurrencyCode();

        ExchangeRate exchangeRate = exchangeRateDao.findByCodePair(updatedExchangeRate.currencyCodePair())
                .orElseThrow(() -> new ExchangeRateNotFoundException("Обменный курс валют с кодами %s и %s не найден"
                        .formatted(baseCurrencyCode, targetCurrencyCode)));

        exchangeRate = exchangeRateDao.update(exchangeRate, new BigDecimal(updatedExchangeRate.rate()));

        return exchangeRateMapper.toExchangeRateResponseDto(exchangeRate);
    }

    @Override
    public boolean existsByCodePair(CurrencyCodePair currencyCodePair) {
        return exchangeRateDao.findByCodePair(currencyCodePair).isPresent();
    }
}

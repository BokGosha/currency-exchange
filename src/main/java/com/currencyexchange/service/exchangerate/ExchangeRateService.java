package com.currencyexchange.service.exchangerate;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.dto.exchangerate.ExchangeRateResponseDto;

import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRateResponseDto> findAll();

    ExchangeRateResponseDto getByCodePair(CurrencyCodePair currencyCodePair);

    ExchangeRateResponseDto create(ExchangeRateRequestDto newExchangeRate);

    ExchangeRateResponseDto update(ExchangeRateRequestDto updatedExchangeRate);

    boolean existsByCodePair(CurrencyCodePair currencyCodePair);
}

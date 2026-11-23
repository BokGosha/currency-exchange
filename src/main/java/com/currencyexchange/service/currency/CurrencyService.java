package com.currencyexchange.service.currency;

import com.currencyexchange.dto.currency.CurrencyRequestDto;
import com.currencyexchange.dto.currency.CurrencyResponseDto;
import com.currencyexchange.model.Currency;

import java.util.List;

public interface CurrencyService {

    List<CurrencyResponseDto> findAll();

    CurrencyResponseDto create(CurrencyRequestDto newCurrency);

    CurrencyResponseDto getByCode(String code);

    Currency getEntityByCode(String code);
}

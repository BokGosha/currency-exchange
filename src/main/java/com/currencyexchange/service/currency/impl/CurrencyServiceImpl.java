package com.currencyexchange.service.currency.impl;

import com.currencyexchange.dao.currency.CurrencyDao;
import com.currencyexchange.dto.currency.CurrencyRequestDto;
import com.currencyexchange.dto.currency.CurrencyResponseDto;
import com.currencyexchange.exception.CurrencyAlreadyExistsException;
import com.currencyexchange.exception.CurrencyNotFoundException;
import com.currencyexchange.mapper.CurrencyMapper;
import com.currencyexchange.model.Currency;
import com.currencyexchange.service.currency.CurrencyService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDao currencyDao;

    private final CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;

    @Override
    public List<CurrencyResponseDto> findAll() {
        return currencyMapper.toCurrencyResponseDtoList(currencyDao.findAll());
    }

    @Override
    public CurrencyResponseDto create(CurrencyRequestDto newCurrency) {
        if (currencyDao.findByCode(newCurrency.code()).isPresent()) {
            throw new CurrencyAlreadyExistsException("Валюта с кодом %s уже существует".formatted(newCurrency.code()));
        }

        Currency currency = currencyMapper.toCurrency(newCurrency);

        currency = currencyDao.create(currency);

        return currencyMapper.toCurrencyResponseDto(currency);
    }

    @Override
    public Currency getEntityByCode(String code) {
        return currencyDao.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException("Валюта с кодом %s не найдена".formatted(code)));
    }

    @Override
    public CurrencyResponseDto getByCode(String code) {
        return currencyMapper.toCurrencyResponseDto(getEntityByCode(code));
    }
}

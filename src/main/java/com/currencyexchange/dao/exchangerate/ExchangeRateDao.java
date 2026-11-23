package com.currencyexchange.dao.exchangerate;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDao {

    List<ExchangeRate> findAll();

    Optional<ExchangeRate> findByCodePair(CurrencyCodePair currencyCodePair);

    ExchangeRate create(ExchangeRate exchangeRate);

    ExchangeRate update(ExchangeRate exchangeRate, BigDecimal newRate);
}

package com.currencyexchange.dao.currency;

import com.currencyexchange.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    List<Currency> findAll();

    Currency create(Currency currency);

    Optional<Currency> findByCode(String code);
}

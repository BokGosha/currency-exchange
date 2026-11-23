package com.currencyexchange.dto.exchangerate;

import com.currencyexchange.dto.currency.CurrencyCodePair;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(CurrencyCodePair currencyCodePair, String rate) {
}

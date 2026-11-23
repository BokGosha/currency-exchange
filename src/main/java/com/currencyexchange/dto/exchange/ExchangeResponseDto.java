package com.currencyexchange.dto.exchange;

import com.currencyexchange.dto.currency.CurrencyResponseDto;

import java.math.BigDecimal;

public record ExchangeResponseDto (
        CurrencyResponseDto baseCurrency,
        CurrencyResponseDto targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}

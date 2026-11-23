package com.currencyexchange.dto.exchange;

import com.currencyexchange.dto.currency.CurrencyCodePair;

public record ExchangeRequestDto(CurrencyCodePair currencyCodePair, String amount) {
}

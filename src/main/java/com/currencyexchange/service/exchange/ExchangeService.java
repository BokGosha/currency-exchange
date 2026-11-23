package com.currencyexchange.service.exchange;

import com.currencyexchange.dto.exchange.ExchangeRequestDto;
import com.currencyexchange.dto.exchange.ExchangeResponseDto;

public interface ExchangeService {

    ExchangeResponseDto getExchange(ExchangeRequestDto exchangeRequestDto);
}

package com.currencyexchange.mapper;

import com.currencyexchange.dto.exchange.ExchangeResponseDto;
import com.currencyexchange.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeMapper {

    ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

    ExchangeResponseDto toExchangeResponseDto(
            Currency baseCurrency,
            Currency targetCurrency,
            BigDecimal rate,
            BigDecimal amount,
            BigDecimal convertedAmount
    );
}

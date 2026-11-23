package com.currencyexchange.mapper;

import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.dto.exchangerate.ExchangeRateResponseDto;
import com.currencyexchange.model.Currency;
import com.currencyexchange.model.ExchangeRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper {

    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toExchangeRateResponseDto(ExchangeRate exchangeRate);

    List<ExchangeRateResponseDto> toExchangeRateResponseDtoList(List<ExchangeRate> exchangeRates);

    @Mapping(target = "id", ignore = true)
    ExchangeRate toExchangeRate(ExchangeRateRequestDto exchangeRateCreateDto,
                                Currency baseCurrency,
                                Currency targetCurrency);
}

package com.currencyexchange.mapper;

import com.currencyexchange.dto.currency.CurrencyRequestDto;
import com.currencyexchange.dto.currency.CurrencyResponseDto;
import com.currencyexchange.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(target = "id", ignore = true)
    Currency toCurrency(CurrencyRequestDto requestDto);

    List<CurrencyResponseDto> toCurrencyResponseDtoList(List<Currency> currencies);

    CurrencyResponseDto toCurrencyResponseDto(Currency currency);
}

package com.currencyexchange.validator;

import com.currencyexchange.dto.exchangerate.ExchangeRateRequestDto;
import com.currencyexchange.exception.ValidationException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public final class ExchangeRateValidator extends BaseValidator {

    private final CurrencyValidator currencyValidator;

    public void validate(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.currencyCodePair().baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.currencyCodePair().targetCurrencyCode();
        String rate = exchangeRateRequestDto.rate();

        validateNull("baseCurrencyCode", baseCurrencyCode);
        validateNull("targetCurrencyCode", targetCurrencyCode);
        validateNull("rate", rate);

        validateBlank("baseCurrencyCode", baseCurrencyCode);
        validateBlank("targetCurrencyCode", targetCurrencyCode);
        validateBlank("rate", rate);

        currencyValidator.validateCode(baseCurrencyCode);
        currencyValidator.validateCode(targetCurrencyCode);

        validateRate(rate);
    }

    private void validateRate(String rateValue) {
        BigDecimal rate;

        try {
            rate = new BigDecimal(rateValue);
        } catch (NumberFormatException e) {
            throw new ValidationException("Некорректный формат rate %s".formatted(rateValue));
        }

        if (rate.compareTo(BigDecimal.ZERO) < 1) {
            throw new ValidationException("Rate %s должен быть больше 0".formatted(rateValue));
        }
    }
}

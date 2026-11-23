package com.currencyexchange.validator;

import com.currencyexchange.dto.exchange.ExchangeRequestDto;
import com.currencyexchange.exception.ValidationException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public final class ExchangeValidator extends BaseValidator {

    private final CurrencyValidator currencyValidator;

    public void validate(ExchangeRequestDto exchangeRequestDto) {
        String baseCurrencyCode = exchangeRequestDto.currencyCodePair().baseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDto.currencyCodePair().targetCurrencyCode();
        String amount = exchangeRequestDto.amount();

        validateNull("from", baseCurrencyCode);
        validateNull("to", targetCurrencyCode);
        validateNull("amount", amount);

        validateBlank("from", baseCurrencyCode);
        validateBlank("to", targetCurrencyCode);
        validateBlank("amount", amount);

        currencyValidator.validateCode(baseCurrencyCode);
        currencyValidator.validateCode(targetCurrencyCode);

        validateAmount(amount);
    }

    private void validateAmount(String amountValue) {
        BigDecimal amount;

        try {
            amount = new BigDecimal(amountValue);
        } catch (NumberFormatException e) {
            throw new ValidationException("Некорректный формат amount %s".formatted(amountValue));
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Amount %s не должен быть меньше 0".formatted(amountValue));
        }
    }
}

package com.currencyexchange.util;

import com.currencyexchange.dto.currency.CurrencyCodePair;
import com.currencyexchange.exception.ValidationException;

public final class CurrencyCodePairParser {

    private static final int CURRENCY_CODE_LENGTH = 3;
    private static final int CURRENCY_CODE_PAIR_LENGTH = 6;

    private CurrencyCodePairParser() {
    }

    public static CurrencyCodePair parse(String codePair) {
        String normalCodePair = codePair.strip();
        if (normalCodePair.length() != CURRENCY_CODE_PAIR_LENGTH) {
            throw new ValidationException("Некорректная валютная пара, она должна включать в себя 6 символов, получено %d"
                    .formatted(normalCodePair.length()));
        }

        String baseCurrencyCode = normalCodePair.substring(0, CURRENCY_CODE_LENGTH);
        String targetCurrencyCode = normalCodePair.substring(CURRENCY_CODE_LENGTH);

        return new CurrencyCodePair(baseCurrencyCode, targetCurrencyCode);
    }
}

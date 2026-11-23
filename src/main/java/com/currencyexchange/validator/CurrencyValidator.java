package com.currencyexchange.validator;

import com.currencyexchange.dto.currency.CurrencyRequestDto;
import com.currencyexchange.exception.ValidationException;

public final class CurrencyValidator extends BaseValidator {

    private static final int CODE_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 40;
    private static final int MAX_SIGN_LENGTH = 3;

    public void validate(CurrencyRequestDto currencyRequestDto) {
        String code = currencyRequestDto.code();
        String name = currencyRequestDto.name();
        String sign = currencyRequestDto.sign();

        validateNull("code", code);
        validateNull("name", name);
        validateNull("sign", sign);

        validateBlank("code", code);
        validateBlank("name", name);
        validateBlank("sign", sign);

        validateCode(code);
        validateName(name);
        validateSign(sign);
    }

    public void validateCode(String code) {
        if (code.length() != CODE_LENGTH) {
            throw new ValidationException("Код валюты %s должен состоять из 3 символов, получено %d"
                    .formatted(code, code.length()));
        }
        
        boolean consistsOnlyLetter = code.chars().allMatch(c -> c >= 'A' && c <= 'Z');
        if (!consistsOnlyLetter) {
            throw new ValidationException("Код валюты %s должен состоять только из латинских букв".formatted(code));
        }
    }

    private void validateName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new ValidationException("Длина названия валюты %s должна быть не более %d"
                    .formatted(name, MAX_NAME_LENGTH));
        }
    }

    private void validateSign(String sign) {
        if (sign.length() > MAX_SIGN_LENGTH) {
            throw new ValidationException("Знак %s должен занимать не более %d символов"
                    .formatted(sign, MAX_SIGN_LENGTH));
        }
    }
}

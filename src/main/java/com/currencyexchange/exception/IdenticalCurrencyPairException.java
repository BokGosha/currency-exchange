package com.currencyexchange.exception;

public class IdenticalCurrencyPairException extends RuntimeException {

    public IdenticalCurrencyPairException(String message) {
        super(message);
    }
}

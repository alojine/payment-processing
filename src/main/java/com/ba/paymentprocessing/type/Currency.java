package com.ba.paymentprocessing.type;

import com.ba.paymentprocessing.exception.RequestValidationException;

public enum Currency {
    EUR,
    USD;

    public static Currency toEnum(String currency) {
        return switch (currency) {
            case "EUR" -> Currency.EUR;
            case "USD" -> Currency.USD;
            default -> throw new RequestValidationException("Incorrect or unsupported type of currency.");
        };
    }
}

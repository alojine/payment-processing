package com.ba.paymentprocessing.type;

public enum Currency {
    EUR,
    USD;

    public static Currency toEnum(String currency) {
        return currency.equals("EUR") ? Currency.EUR : Currency.USD;
    }
}

package com.ba.paymentprocessing.type;

import com.ba.paymentprocessing.exception.RequestValidationException;

public enum PaymentType {
    TYPE1,
    TYPE2,
    TYPE3;

    public static PaymentType toEnum(String paymentType) {
        return switch (paymentType) {
            case "TYPE1" -> PaymentType.TYPE1;
            case "TYPE2" -> PaymentType.TYPE2;
            case "TYPE3" -> PaymentType.TYPE3;
            default -> throw new RequestValidationException("Unsupported type of payment.");
        };
    }
}

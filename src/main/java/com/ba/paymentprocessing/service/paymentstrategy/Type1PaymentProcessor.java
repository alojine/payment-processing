package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Qualifier("type1PaymentProcessor")
public class Type1PaymentProcessor implements PaymentProcessor {
    @Override
    public Payment validate(Payment payment, PaymentRequestDTO paymentRequestDTO) {
        if (Currency.toEnum(paymentRequestDTO.currency()) != Currency.EUR)
            throw new RequestValidationException("TYPE1 payment only supports EUR currency");
        payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));

        if (paymentRequestDTO.details() == null || paymentRequestDTO.details().isEmpty())
            throw new RequestValidationException("TYPE1 payment must have details field");
        payment.setDetails(paymentRequestDTO.details());

        return payment;
    }

    @Override
    public BigDecimal calculateCancellationFee(BigDecimal duration) {
        return duration.multiply(BigDecimal.valueOf(0.05));
    }
}

package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Type2PaymentProcessor implements PaymentProcessor{

    @Override
    public Payment validate(Payment payment, PaymentRequestDTO paymentRequestDTO) {
        if (Currency.toEnum(paymentRequestDTO.currency()) != Currency.USD)
            throw new RequestValidationException("TYPE1 payment only supports USD currency");
        payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));

        if (paymentRequestDTO.details() != null){
            payment.setDetails(paymentRequestDTO.details());
            // log that payment details have been set
        }

        return payment;
    }

    @Override
    public void calculateCancellationFee(Payment payment) {
        BigDecimal type2 = BigDecimal.valueOf(0.1);
    }
}

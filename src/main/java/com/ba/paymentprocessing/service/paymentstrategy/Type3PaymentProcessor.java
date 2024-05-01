package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Qualifier("type3PaymentProcessor")
public class Type3PaymentProcessor implements PaymentProcessor{

    @Override
    public Payment validate(Payment payment, PaymentRequestDTO paymentRequestDTO) {
        payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));

        if (paymentRequestDTO.bicCode() == null)
            throw new RequestValidationException("TYPE3 payment must have BIC Code field defined");
        payment.setBicCode(paymentRequestDTO.bicCode());

        return payment;
    }

    @Override
    public void calculateCancellationFee(Payment payment) {
        BigDecimal type3 = BigDecimal.valueOf(0.15);
    }
}

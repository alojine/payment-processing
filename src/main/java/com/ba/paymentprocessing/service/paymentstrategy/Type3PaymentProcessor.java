package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Qualifier("type3PaymentProcessor")
public class Type3PaymentProcessor implements PaymentProcessor{
    private final Logger logger = LoggerFactory.getLogger(Type3PaymentProcessor.class);

    @Override
    public Payment validate(Payment payment, PaymentRequestDTO paymentRequestDTO) {
        payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));

        if (paymentRequestDTO.bicCode() == null)
            throw new RequestValidationException("TYPE3 payment must have BIC Code field defined");
        payment.setBicCode(paymentRequestDTO.bicCode());

        logger.debug("Payment has been validated as TYPE3 payment.");
        return payment;
    }

    @Override
    public BigDecimal calculateCancellationFee(BigDecimal duration) {
        return duration.multiply(BigDecimal.valueOf(0.15));
    }
}

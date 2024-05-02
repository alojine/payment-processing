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
import java.math.RoundingMode;

@Service
@Qualifier("type2PaymentProcessor")
public class Type2PaymentProcessor implements PaymentProcessor{
    private final Logger logger = LoggerFactory.getLogger(Type2PaymentProcessor.class);

    @Override
    public Payment validate(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        if (Currency.toEnum(paymentRequestDTO.currency()) != Currency.USD)
            throw new RequestValidationException("TYPE1 payment only supports USD currency.");
        payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));

        if (paymentRequestDTO.details() != null){
            payment.setDetails(paymentRequestDTO.details());
            logger.info("Details for payment TYPE2 have been set.");
        }

        logger.debug("Payment has been validated as TYPE2 payment.");
        return payment;
    }

    @Override
    public BigDecimal calculateCancellationFee(BigDecimal duration) {
        return duration.multiply(BigDecimal.valueOf(0.1)).setScale(2, RoundingMode.CEILING);
    }
}

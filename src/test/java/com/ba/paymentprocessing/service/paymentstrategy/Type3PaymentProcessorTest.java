package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Type3PaymentProcessorTest {

    @InjectMocks
    private Type3PaymentProcessor type3PaymentProcessor;

    @Test
    void whenValidate_thenReturnValidatedPayment() {
        assertThat(type3PaymentProcessor.validate(providePaymentRequestDTO())).isEqualTo(provideValidatedPayment());
    }

    @Test
    void whenValidatePayment_withNoDetails_thenThrowRequestValidationException() {
        PaymentRequestDTO requestDTO = provideNoBicCodePaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type3PaymentProcessor.validate(requestDTO));
    }

    static Payment provideValidatedPayment() {
        Payment payment = new Payment();
        payment.setCurrency(Currency.EUR);
        payment.setBicCode("HBUKGB4B");

        return payment;
    }

    static PaymentRequestDTO providePaymentRequestDTO() {
        return new PaymentRequestDTO(
                "TYPE3",
                BigDecimal.TEN,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "",
                "HBUKGB4B"
        );
    }

    static PaymentRequestDTO provideNoBicCodePaymentRequestDTO() {
        return new PaymentRequestDTO(
                "TYPE3",
                BigDecimal.TEN,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "",
                ""
        );
    }
}
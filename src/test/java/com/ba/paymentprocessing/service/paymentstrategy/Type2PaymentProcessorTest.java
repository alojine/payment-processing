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
class Type2PaymentProcessorTest {

    @InjectMocks
    private Type2PaymentProcessor type2PaymentProcessor;

    @Test
    void whenValidate_thenReturnValidatedPayment() {
        assertThat(type2PaymentProcessor.validate(providePaymentRequestDTO())).isEqualTo(provideValidatedPayment());
    }

    @Test
    void whenValidatePayment_withNotValidCurrency_thenThrowRequestValidationException() {
        PaymentRequestDTO requestDTO = provideWrongCurrencyPaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type2PaymentProcessor.validate(requestDTO));
    }

    static Payment provideValidatedPayment() {
        Payment payment = new Payment();
        payment.setCurrency(Currency.USD);
        payment.setDetails("Payment for car repair");

        return payment;
    }

    static PaymentRequestDTO providePaymentRequestDTO() {
        return new PaymentRequestDTO(
                "TYPE1",
                BigDecimal.TEN,
                "USD",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment for car repair",
                ""
        );
    }

    static PaymentRequestDTO provideWrongCurrencyPaymentRequestDTO() {
        return new PaymentRequestDTO(
                "TYPE1",
                BigDecimal.TEN,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment for car repair",
                ""
        );
    }
}
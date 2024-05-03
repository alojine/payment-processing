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
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class Type1PaymentProcessorStrategyTest {

    @InjectMocks
    private Type1PaymentProcessorStrategy type1PaymentProcessor;

    @Test
    void whenValidate_thenReturnValidatedPayment() {
        assertThat(type1PaymentProcessor.validate(providePaymentRequestDTO())).isEqualTo(provideValidatedPayment());
    }

    @Test
    void whenValidatePayment_withNotValidCurrency_thenThrowRequestValidationException() {
        PaymentRequestDTO requestDTO = provideWrongCurrencyPaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type1PaymentProcessor.validate(requestDTO));
    }

    @Test
    void whenValidatePayment_withNoDetails_thenThrowRequestValidationException() {
        PaymentRequestDTO requestDTO = provideNoDetailsPaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type1PaymentProcessor.validate(requestDTO));
    }

    @Test
    void whenCalculatePayment_returnCalculatedBigDecimal() {
        assertThat(type1PaymentProcessor.calculateCancellationFee(BigDecimal.TEN)).isEqualTo(BigDecimal.valueOf(0.50).setScale(2, RoundingMode.CEILING));
    }


    static Payment provideValidatedPayment() {
        Payment payment = new Payment();
        payment.setCurrency(Currency.EUR);
        payment.setDetails("Payment for car repair");

        return payment;
    }

    static PaymentRequestDTO providePaymentRequestDTO() {
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

    static PaymentRequestDTO provideWrongCurrencyPaymentRequestDTO() {
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

    static PaymentRequestDTO provideNoDetailsPaymentRequestDTO() {
        return new PaymentRequestDTO(
                "TYPE1",
                BigDecimal.TEN,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "",
                ""
        );
    }
}
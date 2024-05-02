package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.type.Currency;
import com.ba.paymentprocessing.type.PaymentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class Type1PaymentProcessorTest {

    @InjectMocks
    private Type1PaymentProcessor type1PaymentProcessor;

    @Test
    void whenValidate_thenReturnValidatedPayment() {
        assertThat(type1PaymentProcessor.validate(providePayment(), providePaymentRequestDTO())).isEqualTo(provideValidatedPayment());
    }

    @Test
    void whenValidatePayment_withNotValidCurrency_thenThrowRequestValidationException() {
        Payment payment = providePayment();
        PaymentRequestDTO requestDTO = provideWrongCurrencyPaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type1PaymentProcessor.validate(payment, requestDTO));
    }

    @Test
    void whenValidatePayment_withNoDetails_thenThrowRequestValidationException() {
        Payment payment = providePayment();
        PaymentRequestDTO requestDTO = provideNoDetailsPaymentRequestDTO();
        assertThrows(RequestValidationException.class, () -> type1PaymentProcessor.validate(payment, requestDTO));
    }


    static Payment provideValidatedPayment() {
        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.toEnum("TYPE1"));
        payment.setAmount(BigDecimal.TEN);
        payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCanceled(false);
        payment.setCurrency(Currency.EUR);
        payment.setDetails("Payment for car repair");

        return payment;
    }

    static Payment providePayment() {
        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.toEnum("TYPE1"));
        payment.setAmount(BigDecimal.TEN);
        payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCanceled(false);

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
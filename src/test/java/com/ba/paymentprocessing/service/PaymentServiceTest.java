package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.dto.PaymentResponseDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.exception.ResourceNotFoundException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.repository.PaymentRepository;
import com.ba.paymentprocessing.service.paymentstrategy.PaymentProcessor;
import com.ba.paymentprocessing.type.Currency;
import com.ba.paymentprocessing.type.PaymentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    @Qualifier("type1PaymentProcessor")
    private PaymentProcessor type1PaymentProcessor;

    @Mock
    @Qualifier("type2PaymentProcessor")
    private PaymentProcessor type2PaymentProcessor;

    @Mock
    @Qualifier("type3PaymentProcessor")
    private PaymentProcessor type3PaymentProcessor;

    @Test
    void getPaymentById_returnPaymentResponseDto() {
        UUID id = UUID.fromString("ba354a83-b969-4eb3-96d7-f268ab98f0a5");
        when(paymentRepository.findById(any())).thenReturn(Optional.of(providePayment()));
        assertEquals(paymentService.getPaymentById(id), providePaymentResponseDTO());
    }

    @Test
    void getPaymentById_throwResourceNotFoundException() {
        UUID id = UUID.fromString("ba354a83-b969-4eb3-96d7-f268ab98f0a5");
        when(paymentRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(id));
    }

    @Test
    void getFilteredPayments_throwReturnMaxAmountFilteredPayments() {
        BigDecimal minAmount = BigDecimal.valueOf(5);
        BigDecimal maxAmount = BigDecimal.valueOf(10);
        UUID expectedId = UUID.fromString("ba354a83-b969-4eb3-96d7-f268ab98f0a5");

        when(paymentRepository.findAll()).thenReturn(providePaymentList());
        assertEquals(paymentService.getFilteredPayments(minAmount, maxAmount), List.of(expectedId));
    }

    @Test
    void getFilteredPayments_throwReturnMinAmountFilteredPayments() {
        BigDecimal minAmount = BigDecimal.valueOf(15);
        BigDecimal maxAmount = BigDecimal.valueOf(100);
        UUID expectedId = UUID.fromString("3adebdfe-e16c-4cc4-969c-c8a03ec88e5e");

        when(paymentRepository.findAll()).thenReturn(providePaymentList());
        assertEquals(paymentService.getFilteredPayments(minAmount, maxAmount), List.of(expectedId));
    }

    @Test
    void getFilteredPayments_withNoBounds_throwReturnEmptyList() {
        BigDecimal minAmount = BigDecimal.valueOf(15);
        BigDecimal maxAmount = BigDecimal.valueOf(25);

        when(paymentRepository.findAll()).thenReturn(providePaymentList());
        assertEquals(paymentService.getFilteredPayments(minAmount, maxAmount), List.of());
    }

    @Test
    void createPayment_withNoAmount_thenThrowRequestValidationException() {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(
                "TYPE1",
                null,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment for car repair",
                ""
        );
        assertThrows(RequestValidationException.class, () -> paymentService.createPayment(paymentRequestDTO));
    }

    @Test
    void createPayment_returnType1Payment() {
        Payment type1Payment = new Payment();
        type1Payment.setPaymentType(PaymentType.TYPE1);
        type1Payment.setAmount(BigDecimal.TEN);
        type1Payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        type1Payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        type1Payment.setCanceled(false);
        type1Payment.setCurrency(Currency.EUR);
        type1Payment.setDetails("Payment for car repair");

        Payment validatedType1Payment = new Payment();
        validatedType1Payment.setCurrency(Currency.EUR);
        validatedType1Payment.setDetails("Payment for car repair");

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(
                "TYPE1",
                BigDecimal.TEN,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment for car repair",
                ""
        );

        when(type1PaymentProcessor.validate(any())).thenReturn(validatedType1Payment);
        paymentService.createPayment(paymentRequestDTO);
        verify(paymentRepository, times(1)).save(type1Payment);
    }

    @Test
    void createPayment_returnType2Payment() {
        Payment type2Payment = new Payment();
        type2Payment.setPaymentType(PaymentType.TYPE2);
        type2Payment.setAmount(BigDecimal.TEN);
        type2Payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        type2Payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        type2Payment.setCanceled(false);
        type2Payment.setCurrency(Currency.USD);
        type2Payment.setDetails("Payment for car repair");

        Payment validatedType2Payment = new Payment();
        validatedType2Payment.setCurrency(Currency.USD);
        validatedType2Payment.setDetails("Payment for car repair");

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(
                "TYPE2",
                BigDecimal.TEN,
                "USD",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment for car repair",
                ""
        );

        when(type2PaymentProcessor.validate(any())).thenReturn(validatedType2Payment);
        paymentService.createPayment(paymentRequestDTO);
        verify(paymentRepository, times(1)).save(type2Payment);
    }

    @Test
    void createPayment_returnType3Payment() {
        Payment type3Payment = new Payment();
        type3Payment.setPaymentType(PaymentType.TYPE3);
        type3Payment.setAmount(BigDecimal.TEN);
        type3Payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        type3Payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        type3Payment.setCanceled(false);
        type3Payment.setCurrency(Currency.USD);
        type3Payment.setBicCode("HBUKGB4B - HBUK");

        Payment validatedType3Payment = new Payment();
        validatedType3Payment.setCurrency(Currency.USD);
        validatedType3Payment.setBicCode("HBUKGB4B - HBUK");

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(
                "TYPE3",
                BigDecimal.TEN,
                "USD",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                null,
                "HBUKGB4B - HBUK"
        );

        when(type3PaymentProcessor.validate(any())).thenReturn(validatedType3Payment);
        paymentService.createPayment(paymentRequestDTO);
        verify(paymentRepository, times(1)).save(type3Payment);
    }

    static List<Payment> providePaymentList() {
        Payment payment = new Payment();
        payment.setId(UUID.fromString("3adebdfe-e16c-4cc4-969c-c8a03ec88e5e"));
        payment.setPaymentType(PaymentType.toEnum("TYPE2"));
        payment.setAmount(BigDecimal.valueOf(30));
        payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCanceled(false);

        return List.of(
                payment,
                providePayment()
        );
    }

    static PaymentResponseDTO providePaymentResponseDTO() {
        return new PaymentResponseDTO(
                UUID.fromString("ba354a83-b969-4eb3-96d7-f268ab98f0a5"),
                null
        );
    }

    static Payment providePayment() {
        Payment payment = new Payment();
        payment.setId(UUID.fromString("ba354a83-b969-4eb3-96d7-f268ab98f0a5"));
        payment.setPaymentType(PaymentType.toEnum("TYPE1"));
        payment.setAmount(BigDecimal.TEN);
        payment.setDebtOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCreditOrIban("IE29 AIBK 9311 5212 3456 78");
        payment.setCanceled(false);

        return payment;
    }
}
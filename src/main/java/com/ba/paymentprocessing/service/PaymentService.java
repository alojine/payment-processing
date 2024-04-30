package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.dto.PaymentByIdResponseDto;
import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.exception.ResourceNotFoundException;
import com.ba.paymentprocessing.repository.PaymentRepository;
import com.ba.paymentprocessing.type.Currency;
import com.ba.paymentprocessing.type.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<UUID> getNotCanceledFilteredPayments(BigDecimal lowerBound, BigDecimal upperBound) {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .filter(payment -> !payment.isCanceled() &&
                        (lowerBound == null || payment.getAmount().compareTo(lowerBound) >= 0) &&
                        (upperBound== null || payment.getAmount().compareTo(upperBound) <= 0))
                .map(Payment::getId)
                .toList();
    }

    public PaymentByIdResponseDto getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Payment with Id: %s does not exist", id)));

        return new PaymentByIdResponseDto(
                payment.getId(),
                BigDecimal.ONE
        );
    }

    public Payment createPayment(PaymentRequestDTO paymentRequestDTO) {

        Payment payment = new Payment();
//        implement logging service
        payment.setPaymentType(PaymentType.toEnum(paymentRequestDTO.paymentType()));
        payment.setAmount(paymentRequestDTO.amount());
        payment.setDebtOrIban(paymentRequestDTO.debtOrIban());
        payment.setCreditOrIban(paymentRequestDTO.creditOrIban());

        if (payment.getPaymentType() == PaymentType.TYPE1) {
            payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));
            if (payment.getCurrency() != Currency.EUR) {
                throw new RequestValidationException("TYPE1 payment only supports EUR currency");
            }
            payment.setDetails(paymentRequestDTO.details());
        } else if (payment.getPaymentType() == PaymentType.TYPE2) {
            payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));
            if (payment.getCurrency() != Currency.USD) {
                throw new RequestValidationException("TYPE2 payment only supports USD currency");
            }
            if (paymentRequestDTO.details() != null)
                payment.setDetails(paymentRequestDTO.details());

        } else if (payment.getPaymentType() == PaymentType.TYPE3) {
            payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));
            payment.setBicCode(paymentRequestDTO.bicCode());
        }

        return paymentRepository.save(payment);
    }
}

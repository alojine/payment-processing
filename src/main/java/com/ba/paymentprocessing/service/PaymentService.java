package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.domain.DTO.PaymentRequestDTO;
import com.ba.paymentprocessing.domain.model.Payment;
import com.ba.paymentprocessing.repository.PaymentRepository;
import com.ba.paymentprocessing.type.Currency;
import com.ba.paymentprocessing.type.PaymentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
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
                throw new IllegalArgumentException("must be eur");
            }
            payment.setDetails(paymentRequestDTO.details());
        } else if (payment.getPaymentType() == PaymentType.TYPE2) {
            payment.setCurrency(Currency.toEnum(paymentRequestDTO.currency()));
            if (payment.getCurrency() != Currency.USD) {
                throw new IllegalArgumentException("must be usd");
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

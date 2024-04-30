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
        payment.setAmount(paymentRequestDTO.amount());
        payment.setDebtOrIban(paymentRequestDTO.debtOrIban());
        payment.setCreditOrIban(paymentRequestDTO.creditOrIban());

        if (paymentRequestDTO.paymentType() == PaymentType.TYPE1) {
            if (paymentRequestDTO.currency() != Currency.EUR) {
                throw new IllegalArgumentException("must be eur");
            }
            payment.setCurrency(paymentRequestDTO.currency());
            payment.setDetails(paymentRequestDTO.details());

        } else if (paymentRequestDTO.paymentType() == PaymentType.TYPE2) {
            if (paymentRequestDTO.currency() != Currency.USD) {
                throw new IllegalArgumentException("must be usd");
            }
            payment.setCurrency(paymentRequestDTO.currency());
            if (paymentRequestDTO.details() != null)
                payment.setDetails(paymentRequestDTO.details());

        } else if (paymentRequestDTO.paymentType() == PaymentType.TYPE3) {
            payment.setBicCode(paymentRequestDTO.bicCode());
        }

        return paymentRepository.save(payment);
    }
}

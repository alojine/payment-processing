package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.domain.DTO.PaymentRequestDTO;
import com.ba.paymentprocessing.domain.model.Payment;
import com.ba.paymentprocessing.repository.PaymentRepository;
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


        if (paymentRequestDTO.paymentType() == PaymentType.TYPE1) {

        } else if (paymentRequestDTO.paymentType() == PaymentType.TYPE2) {

        } else if (paymentRequestDTO.paymentType() == PaymentType.TYPE3) {

        }

        return payment;
    }
}

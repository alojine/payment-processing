package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.model.Payment;

public interface PaymentProcessor {
    Payment validate(Payment payment, PaymentRequestDTO paymentRequestDTO);
    void calculateCancellationFee(Payment payment);
}

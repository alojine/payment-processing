package com.ba.paymentprocessing.service.paymentstrategy;

import com.ba.paymentprocessing.model.Payment;

public interface PaymentProcessor {
    void createPayment(Payment payment);
    void calculateCancellationFee(Payment payment);
}

package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.dto.PaymentResponseDTO;
import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.exception.ResourceNotFoundException;
import com.ba.paymentprocessing.repository.PaymentRepository;
import com.ba.paymentprocessing.service.paymentstrategy.PaymentProcessor;
import com.ba.paymentprocessing.type.PaymentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessor type1PaymentProcessor;
    private final PaymentProcessor type2PaymentProcessor;
    private final PaymentProcessor type3PaymentProcessor;

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          @Qualifier("type1PaymentProcessor") PaymentProcessor type1PaymentProcessor,
                          @Qualifier("type2PaymentProcessor") PaymentProcessor type2PaymentProcessor,
                          @Qualifier("type3PaymentProcessor") PaymentProcessor type3PaymentProcessor) {
        this.paymentRepository = paymentRepository;
        this.type1PaymentProcessor = type1PaymentProcessor;
        this.type2PaymentProcessor = type2PaymentProcessor;
        this.type3PaymentProcessor = type3PaymentProcessor;
    }

    private Payment getById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Payment with Id: %s does not exist", id)));
    }

    public List<UUID> getFilteredPayments(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .filter(payment -> !payment.isCanceled() &&
                        (minAmount == null || payment.getAmount().compareTo(minAmount) >= 0) &&
                        (maxAmount== null || payment.getAmount().compareTo(maxAmount) <= 0))
                .map(Payment::getId)
                .toList();
    }

    public PaymentResponseDTO getPaymentById(UUID id) {
        Payment payment = getById(id);
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getCancellationFee()
        );
    }

    public void createPayment(PaymentRequestDTO paymentRequestDTO) {
        logger.info("Payment creation process has started");

        if (paymentRequestDTO.paymentType() == null || paymentRequestDTO.amount() == null ||
        paymentRequestDTO.currency() == null || paymentRequestDTO.debtOrIban() == null || paymentRequestDTO.creditOrIban() == null)
            throw new RequestValidationException("Payment is missing some mandatory information.");

        PaymentType paymentType = PaymentType.toEnum(paymentRequestDTO.paymentType());
        PaymentProcessor paymentProcessor;
        if (paymentType == PaymentType.TYPE1) {
            paymentProcessor = type1PaymentProcessor;
        } else if (paymentType == PaymentType.TYPE2) {
            paymentProcessor = type2PaymentProcessor;
        } else {
            paymentProcessor = type3PaymentProcessor;
        }

        Payment payment = paymentProcessor.validate(paymentRequestDTO);
        payment.setPaymentType(paymentType);
        payment.setAmount(paymentRequestDTO.amount());
        payment.setDebtOrIban(paymentRequestDTO.debtOrIban());
        payment.setCreditOrIban(paymentRequestDTO.creditOrIban());
        payment.setCanceled(false);


        paymentRepository.save(payment);
        logger.info("Payment creation process has finished.");
    }

    public void cancelPayment(UUID id) {
        PaymentProcessor paymentProcessor;
        LocalDateTime currentDateTime = LocalDateTime.now();
        Payment payment = getById(id);
        LocalDateTime paymentCreationDateTime = payment.getCreatedAt().toLocalDateTime();
        logger.info("Payment cancellation process has started");

        if (payment.isCanceled()) throw new RequestValidationException(String.format("The payment with Id: %s is already cancelled.", id));

        if (currentDateTime.toLocalDate().isEqual(paymentCreationDateTime.toLocalDate())) {
            // Is 0:55 1 or 0 | for now its 1
            BigDecimal duration = BigDecimal.valueOf(Math.max(Duration.between(paymentCreationDateTime, currentDateTime).toHours(), 1));
            if (payment.getPaymentType() == PaymentType.TYPE1) {
                paymentProcessor = type1PaymentProcessor;
            } else if (payment.getPaymentType() == PaymentType.TYPE2) {
                paymentProcessor = type2PaymentProcessor;
            } else {
                paymentProcessor = type3PaymentProcessor;
            }
            payment.setCancellationFee(paymentProcessor.calculateCancellationFee(duration));
            payment.setCanceled(true);
            paymentRepository.save(payment);
            logger.info("Payment cancellation process has finished");
        } else throw new RequestValidationException("Payment can only be cancelled on the same day it was created.");
    }
}

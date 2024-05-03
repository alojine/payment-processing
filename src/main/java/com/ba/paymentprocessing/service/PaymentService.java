package com.ba.paymentprocessing.service;

import com.ba.paymentprocessing.dto.PaymentResponseDTO;
import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.RequestValidationException;
import com.ba.paymentprocessing.model.Payment;
import com.ba.paymentprocessing.exception.ResourceNotFoundException;
import com.ba.paymentprocessing.repository.PaymentRepository;
import com.ba.paymentprocessing.service.paymentstrategy.PaymentProcessorStrategy;
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
    private final PaymentProcessorStrategy type1PaymentProcessorStrategy;
    private final PaymentProcessorStrategy type2PaymentProcessorStrategy;
    private final PaymentProcessorStrategy type3PaymentProcessorStrategy;

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          @Qualifier("type1PaymentProcessorStrategy") PaymentProcessorStrategy type1PaymentProcessorStrategy,
                          @Qualifier("type2PaymentProcessorStrategy") PaymentProcessorStrategy type2PaymentProcessorStrategy,
                          @Qualifier("type3PaymentProcessorStrategy") PaymentProcessorStrategy type3PaymentProcessorStrategy) {
        this.paymentRepository = paymentRepository;
        this.type1PaymentProcessorStrategy = type1PaymentProcessorStrategy;
        this.type2PaymentProcessorStrategy = type2PaymentProcessorStrategy;
        this.type3PaymentProcessorStrategy = type3PaymentProcessorStrategy;
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
        logger.info("Payment creation process has started.");

        if (paymentRequestDTO.paymentType().isEmpty() || paymentRequestDTO.amount() == null ||
        paymentRequestDTO.currency().isEmpty() || paymentRequestDTO.debtOrIban().isEmpty() || paymentRequestDTO.creditOrIban().isEmpty())
            throw new RequestValidationException("Payment is missing some mandatory information.");

        PaymentType paymentType = PaymentType.toEnum(paymentRequestDTO.paymentType());
        PaymentProcessorStrategy paymentProcessorStrategy = findPaymentProcessor(paymentType);

        Payment payment = paymentProcessorStrategy.validate(paymentRequestDTO);
        payment.setPaymentType(paymentType);
        payment.setAmount(paymentRequestDTO.amount());
        payment.setDebtOrIban(paymentRequestDTO.debtOrIban());
        payment.setCreditOrIban(paymentRequestDTO.creditOrIban());
        payment.setCanceled(false);

        paymentRepository.save(payment);
        logger.info("Payment creation process has finished.");
    }

    public void cancelPayment(UUID id) {
        Payment payment = getById(id);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime paymentCreationDateTime = payment.getCreatedAt().toLocalDateTime();
        logger.info("Payment cancellation process has started.");

        if (payment.isCanceled())
            throw new RequestValidationException(String.format("The payment with Id: %s is already cancelled.", id));

        long hoursSinceCreation = Duration.between(paymentCreationDateTime, currentDateTime).toHours();
        if (hoursSinceCreation > 24)
            throw new RequestValidationException("Payment can only be cancelled on the same day it was created.");

        PaymentProcessorStrategy paymentProcessorStrategy = findPaymentProcessor(payment.getPaymentType());
        BigDecimal duration = BigDecimal.valueOf(Duration.between(paymentCreationDateTime, currentDateTime).toHours());

        payment.setCancellationFee(paymentProcessorStrategy.calculateCancellationFee(duration));
        payment.setCanceled(true);
        paymentRepository.save(payment);

        logger.info("Payment cancellation process has finished.");
    }

    private Payment getById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Payment with Id: %s does not exist", id)));
    }

    private PaymentProcessorStrategy findPaymentProcessor(PaymentType paymentType) {
        return switch (paymentType) {
            case PaymentType.TYPE1 -> type1PaymentProcessorStrategy;
            case PaymentType.TYPE2 -> type2PaymentProcessorStrategy;
            default -> type3PaymentProcessorStrategy;
        };
    }
}

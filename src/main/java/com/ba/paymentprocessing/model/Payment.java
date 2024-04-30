package com.ba.paymentprocessing.model;

import com.ba.paymentprocessing.type.Currency;
import com.ba.paymentprocessing.type.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
public class Payment {
    @GeneratedValue
    @Id
    private UUID id;

    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "debt_or_iban", nullable = false)
    private String debtOrIban;

    @Column(name = "credit_or_iban", nullable = false)
    private String creditOrIban;

    @Column(name = "details")
    private String details;

    @Column(name = "bic_code")
    private String bicCode;

    // status?
    // fees?

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;
}

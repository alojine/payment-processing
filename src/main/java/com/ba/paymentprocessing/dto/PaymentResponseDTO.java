package com.ba.paymentprocessing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponseDTO(
        UUID id,
        BigDecimal cancellationFee
) {
}

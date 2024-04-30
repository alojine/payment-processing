package com.ba.paymentprocessing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentByIdResponseDto(
        UUID id,
        BigDecimal cancellationFee
) {
}

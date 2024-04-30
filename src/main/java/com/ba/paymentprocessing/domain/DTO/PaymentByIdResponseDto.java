package com.ba.paymentprocessing.domain.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentByIdResponseDto(
        UUID id,
        BigDecimal cancellationFee
) {
}

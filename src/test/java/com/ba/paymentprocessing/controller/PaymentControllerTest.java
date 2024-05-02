package com.ba.paymentprocessing.controller;

import com.ba.paymentprocessing.dto.PaymentByIdResponseDto;
import com.ba.paymentprocessing.dto.PaymentRequestDTO;
import com.ba.paymentprocessing.exception.ResourceNotFoundException;
import com.ba.paymentprocessing.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private static final UUID uuid = UUID.fromString("661cd653-b5b3-44bf-9864-8cf0b35f3a21");

    @Test
    void whenGetFilteredPayments_thenReturnPayments() throws Exception {
        when(paymentService.getFilteredPayments(any(), any())).thenReturn(providePaymentUUIDList());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/payments")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andReturn();
    }

    @Test
    void whenGetPaymentById_withNotExistingId_thenThrowResourceNotFoundError() throws Exception {
        UUID uuid = UUID.fromString("661cd653-b5b3-44bf-9864-8cf0b35f3a21");
        when(paymentService.getPaymentById(any())).thenThrow(new ResourceNotFoundException(String.format("Payment with Id: %s does not exist", uuid)));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/payments/{uuid}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void whenGetPaymentById_thenReturnPaymentByIdResponseDto() throws Exception {
        when(paymentService.getPaymentById(any())).thenReturn(providePaymentByIdResponseDto());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/payments/{uuid}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(providePaymentByIdResponseDto())))
                .andReturn();
    }

    @Test
    void whenCancelPayment_thenReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/payments/{uuid}", uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void whenCreatePayment_thenReturnOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(providePaymentRequestDto())))
                .andExpect(status().isCreated());
    }

    static PaymentRequestDTO providePaymentRequestDto() {
        return new PaymentRequestDTO(
                "TYPE1",
                BigDecimal.ONE,
                "EUR",
                "IE29 AIBK 9311 5212 3456 78",
                "IE29 AIBK 9311 5212 3456 78",
                "Payment fro car repair",
                ""
        );
    }

    static PaymentByIdResponseDto providePaymentByIdResponseDto() {
        return new PaymentByIdResponseDto(
                UUID.fromString("661cd653-b5b3-44bf-9864-8cf0b35f3a21"),
                BigDecimal.ONE
        );
    }

    static List<UUID> providePaymentUUIDList() {
        return List.of(
                UUID.fromString("661cd653-b5b3-44bf-9864-8cf0b35f3a21")
        );
    }
}
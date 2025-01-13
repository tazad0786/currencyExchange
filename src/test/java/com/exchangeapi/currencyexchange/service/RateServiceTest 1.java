package com.exchangeapi.currencyexchange.controller;

import com.exchangeapi.currencyexchange.dto.RateDto;
import com.exchangeapi.currencyexchange.entity.enums.EnumCurrency;
import com.exchangeapi.currencyexchange.service.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RateControllerTest {

    private RateController rateController;

    @Mock
    private RateService rateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateController = new RateController(rateService);
    }

    @Test
    void testGetRates() {
        // Arrange
        EnumCurrency base = EnumCurrency.USD;
        List<EnumCurrency> target = Collections.singletonList(EnumCurrency.EUR);
        LocalDate date = LocalDate.of(2023, 1, 1);
        RateDto rateDto = new RateDto(); // Adjust fields as necessary for RateDto
        when(rateService.calculateRate(base, target, date)).thenReturn(rateDto);

        // Act
        ResponseEntity<RateDto> response = rateController.getRates(base, target, date);

        // Assert
        assertEquals(ResponseEntity.ok(rateDto), response);
        verify(rateService, times(1)).calculateRate(base, target, date);
    }

    @Test
    void testCalculatePayableAmount() {
        // Arrange
        Bill bill = new Bill(); // Set necessary fields for Bill object
        bill.setTotalAmount(1000.0);
        bill.setOriginalCurrency("USD");
        bill.setTargetCurrency("EUR");

        double payableAmount = 900.0;
        double discount = 100.0;
        double exchangeRate = 0.85;

        when(rateService.calculatePayableAmount(bill)).thenReturn(payableAmount);
        when(rateService.calculateDiscount(bill)).thenReturn(discount);
        when(rateService.getExchangeRate(EnumCurrency.USD, EnumCurrency.EUR)).thenReturn(exchangeRate);

        PayableAmountResponse expectedResponse = new PayableAmountResponse(
                1000.0, discount, exchangeRate, payableAmount);

        // Act
        ResponseEntity<PayableAmountResponse> response = rateController.calculatePayableAmount(bill);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(rateService, times(1)).calculatePayableAmount(bill);
        verify(rateService, times(1)).calculateDiscount(bill);
        verify(rateService, times(1)).getExchangeRate(EnumCurrency.USD, EnumCurrency.EUR);
    }
}

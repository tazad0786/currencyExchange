package com.exchangeapi.currencyexchange.payload.response;

import com.exchangeapi.currencyexchange.entity.enums.EnumCurrency;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public record PayableAmountResponse {

private double totalAmount;
private double discount;
private double exchangeRate;
private double payableAmount;
}

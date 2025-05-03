package com.system.card.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String cardNumber;
    private String cardHolderName;
    private LocalDate expiryDate;
    private BigDecimal balance;
}

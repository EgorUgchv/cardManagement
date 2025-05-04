package com.system.card.card;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @MaskData
    private String cardNumber;
    @NotBlank
    private String cardHolderFullName;
    @NotBlank
    private LocalDate expiryDate;
    @Min(0)
    private BigDecimal balance;
    @Email
    private String cardHolderEmail;
}

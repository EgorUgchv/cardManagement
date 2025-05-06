package com.system.card.card;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @Future
    private LocalDate expiryDate;
    @Min(0)
    private BigDecimal balance;
    @Email
    private String cardHolderEmail;

}

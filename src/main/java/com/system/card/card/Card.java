package com.system.card.card;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.system.card.config.Encryptor;
import com.system.card.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue
    private Integer cardId;
    @Convert(converter = Encryptor.class)
    @Column(name = "encrypted_card_number", nullable = false, columnDefinition = "TEXT", unique = true)
    private String encryptedCardNumber;
    @Column(name = "card_holder_name", nullable = false, length = 100)
    private String cardHolderFullName;
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    @Enumerated(EnumType.STRING)
    @Column(name= "card_status", nullable = false, length = 15)
    private CardStatus cardStatus;
    @Column(name = "balance",nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;
}

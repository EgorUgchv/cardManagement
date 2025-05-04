package com.system.card.card;

import com.system.card.mapper.CardMapper;
import com.system.card.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class CardService {
    private CardMapper cardMapper;
    private CardRepository cardRepository;

    public void createCard(@Valid CardDto cardDto, User user) {
        Card card = cardMapper.mapToCard(cardDto);
        card.setUser(user);
        card.setCardStatus(CardStatus.ACTIVE);
        card.setEncryptedCardNumber(cardDto.getCardNumber());
        cardRepository.save(card);
    }

    public List<CardDto> getAllCards() {
        var allCards = cardRepository.findAll();
        return cardMapper.mapToCardDto(allCards);
    }
}

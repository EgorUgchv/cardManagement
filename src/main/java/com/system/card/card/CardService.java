package com.system.card.card;

import com.system.card.mapper.CardMapper;
import com.system.card.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class CardService {
    private CardMapper cardMapper;
    private CardRepository cardRepository;

    /**
     * Создание карты в базе данных
     *
     * @param cardDto карта, которую необходимо создать
     * @param user    пользователь, который существует в базе данных
     */
    @Transactional
    public void createCard(@Valid CardDto cardDto, User user) {
        Card card = cardMapper.mapToCard(cardDto);
        card.setUser(user);
        card.setCardStatus(CardStatus.ACTIVE);
        card.setEncryptedCardNumber(cardDto.getCardNumber());
        cardRepository.save(card);
    }

    /**
     * Получение всех карт
     *
     * @return список всех карт
     */
    public List<CardDto> getAllCards() {
        var allCards = cardRepository.findAll();
        return cardMapper.mapToCardDto(allCards);
    }

    public void changeCardStatus(Optional<Card> card) throws BadRequestException {
        if (card.isPresent()) {
            card.get().setCardStatus(CardStatus.BLOCKED);
            cardRepository.save(card.get());
        } else {
            throw new BadRequestException();
        }

    }

    public Page<CardDto> getAllCardsPage(User user, @Min(0) Integer offset, Integer limit) {
        return cardRepository.findAllByUser(user, PageRequest.of(offset, limit)).map(card -> CardResponse.fromCard(card, user.getEmail()));
    }
}

package com.system.card.card;

import com.system.card.config.JwtService;
import com.system.card.exception.CardAlreadyBlockedException;
import com.system.card.exception.CardBlockedException;
import com.system.card.exception.CardNotFoundException;
import com.system.card.exception.InsufficientFundsException;
import com.system.card.mapper.CardMapper;
import com.system.card.user.User;
import com.system.card.user.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class CardService {
    private final JwtService jwtService;
    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    /**
     * Создание карты в базе данных
     *
     * @param cardDto карта, которую необходимо создать
     */
    @Transactional
    public void createCard(@Valid CardDto cardDto) throws BadRequestException {
        User user = userRepository.findByEmail(cardDto.getCardHolderEmail())
                .orElseThrow(() -> new BadRequestException("User not found, create user first"));
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

    public void changeCardStatus(String cardNumber) throws BadRequestException {
        Optional<Card> userCard = cardRepository.getCardByEncryptedCardNumber(cardNumber);
        if (userCard.isPresent()) {
            if (userCard.get().getCardStatus() == CardStatus.BLOCKED) {
                throw new CardAlreadyBlockedException("Card with this number already blocked: " + cardNumber);
            }
            userCard.get().setCardStatus(CardStatus.BLOCKED);
            cardRepository.save(userCard.get());
        } else {
            throw new BadRequestException("Card with this number does not exist: " + cardNumber);
        }

    }

    public Page<CardDto> getAllCardsPage(String authHeader, @Min(0) Integer offset, Integer limit) {
        User user = getUserByJwt(authHeader);
        return cardRepository.findAllByUser(user, PageRequest.of(offset, limit))
                .map(card -> CardResponse.fromCard(card, user.getEmail()));
    }

    public User getUserByJwt(String jwt) {
        final String payload = jwt.substring(7);
        final String userEmail = jwtService.extractUsername(payload);
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void transferMoney(String senderCardNumber, String beneficiaryCardNumber, BigDecimal transferAmount, String jwt) {
        User senderUser = getUserByJwt(jwt);
        Card senderCard = cardRepository.getCardByEncryptedCardNumberAndUser(senderCardNumber, senderUser)
                .orElseThrow(() -> new CardNotFoundException("Card with number " + senderCardNumber + " not found at user " + senderUser.getEmail()));
        Card beneficiaryCard = cardRepository.getCardByEncryptedCardNumber(beneficiaryCardNumber)
                .orElseThrow(() -> new CardNotFoundException("Card with number " + beneficiaryCardNumber + " not found"));

        checkCardIsActive(senderCard);
        checkCardIsActive(beneficiaryCard);
        checkFundsAreSufficient(senderCard, transferAmount);

        senderCard.setBalance(senderCard.getBalance().subtract(transferAmount));
        beneficiaryCard.setBalance(beneficiaryCard.getBalance().add(transferAmount));

        cardRepository.save(senderCard);
        cardRepository.save(beneficiaryCard);

    }

    private void checkFundsAreSufficient(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(card.getBalance()) < 0) {
            throw new InsufficientFundsException("Insufficient funds to transfer: " + amount + " from card" + card.getEncryptedCardNumber());
        }
    }

    private void checkCardIsActive(Card card) {
        if (card.getCardStatus() != CardStatus.ACTIVE) {
            throw new CardBlockedException("Card is blocked: " + card.getEncryptedCardNumber());
        }

    }
}

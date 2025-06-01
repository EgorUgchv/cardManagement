package com.system.card.card;

import com.system.card.auth.AuthenticationResponse;
import com.system.card.auth.AuthenticationService;
import com.system.card.auth.RegisterRequest;
import com.system.card.user.Role;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
@ActiveProfiles("test")
class CardServiceTest {
    @Autowired
    private CardService cardService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CardRepository cardRepository;


    @Test
    void shouldCreateCard_whenValidCardIsProvided() throws BadRequestException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFullName("John Doe Michel");
        registerRequest.setEmail("test@email.com");
        registerRequest.setPassword("password");
        registerRequest.setRole(Role.USER);

        authenticationService.register(registerRequest);

        CardDto cardDto = new CardDto();
        cardDto.setCardNumber("1234567812345678");
        cardDto.setCardHolderFullName("John Doe Michel");
        cardDto.setExpiryDate(LocalDate.of(2026, 6, 30));
        cardDto.setBalance(new BigDecimal(10));
        cardDto.setCardHolderEmail("test@email.com");
        cardDto.setCardStatus(CardStatus.ACTIVE);


        cardService.createCard(cardDto);

        Optional<Card> savedCard = cardRepository.getCardByEncryptedCardNumber(cardDto.getCardNumber());

        assertAll("Assert Card with CardDto",
                () -> assertEquals(savedCard.get().getEncryptedCardNumber(), cardDto.getCardNumber()),
                () -> assertEquals(savedCard.get().getCardHolderFullName(), cardDto.getCardHolderFullName()),
                () -> assertEquals(savedCard.get().getExpiryDate(), cardDto.getExpiryDate()),
                () -> assertEquals(savedCard.get().getBalance(), cardDto.getBalance()),
                () -> assertEquals(savedCard.get().getCardStatus(), cardDto.getCardStatus())
        );
    }

    @Test
    void shouldTransferMoney_whenValidCardNumbersIsProvided() throws BadRequestException {
        RegisterRequest registerRequestSender = new RegisterRequest();
        registerRequestSender.setFullName("John Doe Michel");
        registerRequestSender.setEmail("testSender@email.com");
        registerRequestSender.setPassword("password");
        registerRequestSender.setRole(Role.USER);

        AuthenticationResponse senderAuthData = authenticationService.register(registerRequestSender);

        String senderJwt = "Bearer " + senderAuthData.getAccessToken();

        CardDto cardSenderDto = new CardDto();
        cardSenderDto.setCardNumber("1234567812345678");
        cardSenderDto.setCardHolderFullName("John Doe Michel");
        cardSenderDto.setExpiryDate(LocalDate.of(2026, 6, 30));
        cardSenderDto.setBalance(new BigDecimal(30));
        cardSenderDto.setCardHolderEmail("testSender@email.com");
        cardSenderDto.setCardStatus(CardStatus.ACTIVE);

        cardService.createCard(cardSenderDto);

        RegisterRequest registerRequestBeneficiary = new RegisterRequest();
        registerRequestBeneficiary.setFullName("Michael Era Evrika");
        registerRequestBeneficiary.setEmail("testBeneficiary@email.com");
        registerRequestBeneficiary.setPassword("password2");
        registerRequestBeneficiary.setRole(Role.USER);

        authenticationService.register(registerRequestBeneficiary);

        CardDto cardBeneficiaryDto = new CardDto();
        cardBeneficiaryDto.setCardNumber("1111111111111111");
        cardBeneficiaryDto.setCardHolderFullName("Michael Era Evrika");
        cardBeneficiaryDto.setExpiryDate(LocalDate.of(2026, 1, 30));
        cardBeneficiaryDto.setBalance(new BigDecimal(10));
        cardBeneficiaryDto.setCardHolderEmail("testBeneficiary@email.com");
        cardBeneficiaryDto.setCardStatus(CardStatus.ACTIVE);

        cardService.createCard(cardBeneficiaryDto);

        BigDecimal transferAmount = new BigDecimal(10);

        cardService.transferMoney(cardSenderDto.getCardNumber(), cardBeneficiaryDto.getCardNumber(), transferAmount, senderJwt);

        Optional<Card> cardSender = cardRepository.getCardByEncryptedCardNumber(cardSenderDto.getCardNumber());
        Optional<Card> cardBeneficiary = cardRepository.getCardByEncryptedCardNumber(cardBeneficiaryDto.getCardNumber());

        assertEquals(cardSender.get().getBalance(), cardSenderDto.getBalance().subtract(transferAmount));
        assertEquals(cardBeneficiary.get().getBalance(), cardBeneficiaryDto.getBalance().add(transferAmount));
    }


}

package com.system.card;

import com.system.card.card.Card;
import com.system.card.card.CardRepository;
import com.system.card.card.CardStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
class CardManagementApplicationTests {
@Autowired
private CardRepository cardRepository;
	@Test
	void contextLoads() {
	}

	@Test
	void insertCard() {
		Card card = new Card();
		card.setCardStatus(CardStatus.ACTIVE);
		card.setCardHolderFullName("Egor");
		card.setBalance(BigDecimal.valueOf(1234.12));
		card.setExpiryDate(LocalDate.of(2028, 11, 12));
		card.setEncryptedCardNumber("123456789234567");
		cardRepository.save(card);
	}

	@Test
	void retrieveCard() {
		Card card = cardRepository.findById(202).get();
		System.out.println(card);
	}
}

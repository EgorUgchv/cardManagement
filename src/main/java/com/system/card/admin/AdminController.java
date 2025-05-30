package com.system.card.admin;

import com.system.card.card.CardDto;
import com.system.card.card.CardRepository;
import com.system.card.card.CardService;
import com.system.card.mapper.CardMapper;
import com.system.card.user.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@AllArgsConstructor
public class AdminController {
    private final CardService cardService;
    private final CardRepository cardRepository;
    private CardMapper cardMapper;
    private UserRepository userRepository;

    /**
     * Метод создания карты
     *
     * @param cardDto карта, которую необходимо добавить
     * @return Http response 200
     * @throws BadRequestException переданные данные некорректны или такого пользователя не существует
     */
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> createCard(@Valid @RequestBody CardDto cardDto) throws BadRequestException {
        cardService.createCard(cardDto);
        return new ResponseEntity<>("Created card successful", HttpStatus.CREATED);
    }

    /**
     * Получение всех карт
     *
     * @return Список всех карт
     */
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<CardDto>> getAllCards() {
        List<CardDto> allCards = cardService.getAllCards();
        if (allCards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allCards, HttpStatus.ACCEPTED);
    }

//    @PatchMapping
//    @PreAuthorize("hasAuthority('admin:update')")
//   public ResponseEntity<String> blockCard(@RequestParam String cardNumber,@Email @RequestParam String userEmail) throws BadRequestException {
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new BadRequestException("User not found, create user first"));
//        Optional<Card> userCard = cardRepository.getCardByEncryptedCardNumber(cardNumber);
//        if(userCard.isPresent()) {
//            if (userCard.get().getCardStatus() == CardStatus.BLOCKED){
//                return new ResponseEntity<>("Card already blocked", HttpStatus.BAD_REQUEST);
//            }
//            cardService.changeCardStatus(userCard);
//        }
//        else return new ResponseEntity<>("Card doesn't exists", HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>("Blocked card successful", HttpStatus.ACCEPTED);
//    }
}

package com.system.card.card;

import com.system.card.admin.AdminController;
import com.system.card.config.JwtService;
import com.system.card.user.UserRepository;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasRole('USER')")
@Validated
@AllArgsConstructor
public class CardController {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private CardRepository cardRepository;
    private CardService cardService;
    private AdminController adminController;

    /**
     * Получение всех карт пользователя c постраничной выдачей
     *
     * @param authHeader jwt token
     * @param offset     cмещение
     * @param limit      максимальное количество требуемых записей
     * @return все карты пользователя с постраничной выдачей
     */
    @GetMapping
    public Page<CardDto> getAll(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return cardService.getAllCardsPage(authHeader, offset, limit);
    }

    /**
     * Блокировка карты пользователя
     *
     * @param cardNumber номер карты, которую нужно заблокировать
     * @return Http response 204
     * @throws BadRequestException номер карты не существует
     */
    @PatchMapping("/block-card")
    public ResponseEntity<Void> requestCardBlocking(@RequestParam String cardNumber) throws BadRequestException {
        cardService.changeCardStatus(cardNumber);
        return ResponseEntity.noContent().build();
    }

    /**
     * Перевод денег с одной карты на другую
     * @param senderCardNumber номер карты отправителя
     * @param beneficiaryCardNumber номер карты получателя
     * @param transferAmount сумма, которую нужно перевести
     * @return Http response 204
     * @throws BadRequestException введенные данные неверны
     */
    @PatchMapping("/card-tranfer")
    public ResponseEntity<Void> transferMoney(@RequestParam String senderCardNumber, @RequestParam String beneficiaryCardNumber, @RequestParam BigDecimal transferAmount) throws BadRequestException {
        cardService.transferMoney(senderCardNumber, beneficiaryCardNumber, transferAmount);
        return ResponseEntity.noContent().build();
    }

}

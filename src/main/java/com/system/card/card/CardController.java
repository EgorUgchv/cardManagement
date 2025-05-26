package com.system.card.card;

import com.system.card.admin.AdminController;
import com.system.card.config.JwtService;
import com.system.card.user.User;
import com.system.card.user.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping
    public Page<CardDto> getAll(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return cardService.getAllCardsPage(user, offset, limit);
    }

    @PatchMapping
    public ResponseEntity<String> requestCardBlocking(@RequestParam String cardNumber) throws BadRequestException {
        Optional<Card> userCard = cardRepository.getCardByEncryptedCardNumber(cardNumber);
        if(userCard.isPresent()) {
            if (userCard.get().getCardStatus() == CardStatus.BLOCKED){
                return new ResponseEntity<>("Card already blocked", HttpStatus.BAD_REQUEST);
            }
            cardService.changeCardStatus(userCard);
        }
        else return new ResponseEntity<>("Card doesn't exists", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Blocked card successful", HttpStatus.ACCEPTED);

    }
}

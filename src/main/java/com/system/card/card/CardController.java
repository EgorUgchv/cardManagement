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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        return cardService.getAllCardsPage(authHeader, offset, limit);
    }

    @PatchMapping("/block-card")
    public ResponseEntity<Void> requestCardBlocking(@RequestParam String cardNumber) throws BadRequestException {
        cardService.changeCardStatus(cardNumber);
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("/card-tranfer")
//    public ResponseEntity<String> requestTransfer(@RequestParam String SenderCardNumber, @RequestParam String BeneficiaryCardNumber) throws BadRequestException {
//       cardService.req
//        Optional<Card> userCard = cardRepository.getCardByEncryptedCardNumber(SendCardNumber);
//        if(userCard.isPresent()) {
//
//        }
//        else return new ResponseEntity<>("Card doesn't exists", HttpStatus.BAD_REQUEST);
//    }

}

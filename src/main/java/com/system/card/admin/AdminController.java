package com.system.card.admin;

import com.system.card.card.CardDto;
import com.system.card.card.CardService;
import com.system.card.mapper.CardMapper;
import com.system.card.user.User;
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
    private CardMapper cardMapper;
    private UserRepository userRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> createCard(@Valid @RequestBody CardDto cardDto) throws BadRequestException {
        User user = userRepository.findByFullName(cardDto.getCardHolderFullName())
                .orElseThrow(() -> new BadRequestException("User not found, create user first"));
        cardService.createCard(cardDto, user);
        return new ResponseEntity<>("Created card successful", HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<CardDto>> getAllCards() {
        List<CardDto> allCards = cardService.getAllCards();
        if (allCards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allCards,HttpStatus.ACCEPTED);
    }

}

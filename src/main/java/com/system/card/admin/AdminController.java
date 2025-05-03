package com.system.card.admin;

import com.system.card.card.Card;
import com.system.card.card.CardDto;
import com.system.card.mapper.CardMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class AdminController {
    private final CardMapper cardMapper;
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String createCard(@RequestBody CardDto cardDto) {
        Card card = cardMapper.mapToCard(cardDto);
        return "POST :: admin";
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET :: admin";
    }

}

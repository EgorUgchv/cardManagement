package com.system.card.token;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.system.card.user.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Long id;
    //@Column(unique = true)
    public String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public boolean revoked;
    public boolean expired;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

}

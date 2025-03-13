package com.imbuka.customer_service.token;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.imbuka.customer_service.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String token;
    private boolean expired;

    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}

package com.example.ecommerce_clean.modules.user.domain.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    private Long id;
    private String token;
    private User user;
    private LocalDateTime expiryDate;
    private boolean revoked;
}

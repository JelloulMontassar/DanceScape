package com.dance.mo.auth.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequest {
    private long amount;
    private String email;
    private String currency;
}

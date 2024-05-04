package com.dance.mo.auth.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponse {
    private String intentID;
    private String clientSecret;
}
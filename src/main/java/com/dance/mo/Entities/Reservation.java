package com.dance.mo.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Competition competition;

    private String paymentMethod;

    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;

    private boolean isPaid;
}


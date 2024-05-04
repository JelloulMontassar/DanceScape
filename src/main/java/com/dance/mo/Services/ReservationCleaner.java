package com.dance.mo.Services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class ReservationCleaner {
    private final ReservationService reservationService;
    public ReservationCleaner(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    //@Scheduled(fixedRate = 3600000)
    @Scheduled(fixedRate = 20000)
    public void cleanExpiredReservations() {
        reservationService.deleteExpiredReservations();
    }
}


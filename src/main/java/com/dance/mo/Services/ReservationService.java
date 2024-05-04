package com.dance.mo.Services;

import com.dance.mo.Entities.Competition;
import com.dance.mo.Entities.Reservation;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(User user, Competition competition, String paymentMethod) {
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCompetition(competition);
        reservation.setPaymentMethod(paymentMethod);
        reservation.setReservationDate(LocalDateTime.now());

        if (paymentMethod.equals("cash")) {

            reservation.setExpirationDate(LocalDateTime.now().plusHours(24));
        }
        else {
            reservation.setPaid(true);
            reservation.setExpirationDate(LocalDateTime.now().plusHours(730));

        }

        return reservationRepository.save(reservation);
    }

    public void deleteExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> expiredReservations = reservationRepository.findReservationByExpirationDateBefore(now);
        for (Reservation reservation : expiredReservations) {
            reservationRepository.delete(reservation);
        }
    }

    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    public List<Reservation> getReservationsForUser(User user) {
        return reservationRepository.findReservationByUser(user);
    }
}


package com.dance.mo.Controller;

import com.dance.mo.Entities.Competition;
import com.dance.mo.Entities.Reservation;
import com.dance.mo.Entities.User;
import com.dance.mo.Services.CompetitionAndTicketingSystemService;
import com.dance.mo.Services.ReservationService;
import com.dance.mo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompetitionAndTicketingSystemService competitionAndTicketingSystemService;
    @PostMapping("/create/{competitionId}/{paymentMethod}")
    public ResponseEntity<Reservation> createReservation(Authentication authentication, @PathVariable Long competitionId, @PathVariable String paymentMethod) {
        User user = userService.getUserByEmail(authentication.getName());
        Competition competition = competitionAndTicketingSystemService.getCompetitionById(competitionId);
        Reservation reservation = reservationService.createReservation(user, competition, paymentMethod);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsForUser(User user) {
        List<Reservation> reservations = reservationService.getReservationsForUser(user);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


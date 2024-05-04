package com.dance.mo.Repositories;

import com.dance.mo.Entities.Reservation;
import com.dance.mo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationByExpirationDateBefore(LocalDateTime currentDateTime);

    List<Reservation> findReservationByUser(User user);
}

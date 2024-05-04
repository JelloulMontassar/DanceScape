package com.dance.mo.Repositories;

import com.dance.mo.Entities.Competition;
import com.dance.mo.Entities.DanceVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DanceVenueRepository extends JpaRepository<DanceVenue, Long> {
    Optional<Object> findByCompetition(Competition competition);
}

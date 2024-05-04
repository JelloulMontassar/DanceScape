package com.dance.mo.Repositories;


import com.dance.mo.Entities.Competition;
import com.dance.mo.Entities.DanceVenue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    boolean existsCompetitionByDateAndDanceVenue(Date date, DanceVenue danceVenue);

    Competition getCompetitionByDanceVenue_DanceVenueId(Long id);
}

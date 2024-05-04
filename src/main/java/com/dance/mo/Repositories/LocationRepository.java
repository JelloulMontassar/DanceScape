package com.dance.mo.Repositories;
import com.dance.mo.Entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

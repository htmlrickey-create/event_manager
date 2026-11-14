package com.event.event_manager.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.event_manager.Entity.Booths;

@Repository 
public interface BoothsRepository extends JpaRepository <Booths,Long> {    
    List<Booths> findByEventId(Long eventId);
    Optional<Booths> findByIdAndEventId(Long Id, Long eventId);
    Long countByEventId(Long eventId);
}

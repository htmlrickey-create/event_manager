package com.event.event_manager.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.event.event_manager.Entity.Events;

@Repository 
public interface EventRepository extends JpaRepository <Events, Long> {
}

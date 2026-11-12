package com.event.event_manager.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.event.event_manager.Entity.SchoolGet;
import java.util.Optional;

public interface SchoolRepository extends JpaRepository<SchoolGet, Long> {
    Optional<SchoolGet> findBySchoolName(String schoolName);
}

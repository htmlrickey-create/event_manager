package com.event.event_manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.event.event_manager.Entity.EventMembers;

@Repository 
public interface EventMemberRepository extends JpaRepository <EventMembers, Long> {

    void deleteByEvent_Id(Long eventId);
    List <EventMembers> findByUser_IdAndRole(Long userId, String role);
    Optional<EventMembers> findByUser_IdAndEvent_Id(Long userId, Long eventId);
    List<EventMembers> findByEvent_Id(Long eventId);
    List<EventMembers> findByUser_Id(Long UserId);
    List<EventMembers> findByUser_Email (String email);
    List<EventMembers> findByEvent_IdAndRole(Long eventId, String role);
    Optional<EventMembers> findByUser_EmailAndRole(String email, String role);
}

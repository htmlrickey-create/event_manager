package com.event.event_manager.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.event.event_manager.Entity.EventMembers;

@Repository 
public interface EventMemberRepository extends JpaRepository <EventMembers, Long> {

    List <EventMembers> findByUser_IdAndRole(Long userId, String role);

    
}

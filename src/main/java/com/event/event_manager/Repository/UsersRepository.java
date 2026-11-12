package com.event.event_manager.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.event_manager.Entity.Users;

@Repository 
public interface UsersRepository extends JpaRepository <Users, Long> {  

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
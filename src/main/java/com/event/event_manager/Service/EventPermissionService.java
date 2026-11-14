package com.event.event_manager.Service;

import org.springframework.stereotype.Service;

import com.event.event_manager.Repository.UsersRepository;
import com.event.event_manager.Repository.EventMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventPermissionService {

    private final UsersRepository usersRepository;
    private final EventMemberRepository eventMemberRepository;

    public boolean isAdmin(String email, Long eventId) {

        return usersRepository.findByEmail(email)
            .flatMap(user ->
                eventMemberRepository.findByUser_IdAndEvent_Id(
                    user.getId(), eventId
                )
            )
            .map(member -> "ADMIN".equals(member.getRole()))
            .orElse(false);
    }

    public boolean isStaff(String email, Long eventId){

        return usersRepository.findByEmail(email).flatMap(user -> eventMemberRepository.findByUser_IdAndEvent_Id(user.getId(), eventId)).map(member -> "STAFF".equals(member.getRole())).orElse(false);
    }
}
package com.event.event_manager.Controller;


import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class StaffEventManageController {

    private final EventMemberRepository eventMemberRepository;
    private final UsersRepository usersRepository;

    @GetMapping ("/staff/eventMG")
    public String selectStaffBooth(Authentication authentication, Model model){

        String email = authentication.getName();

        List<EventMembers> memberships = eventMemberRepository.findByUser_Email(email);

        List<EventMembers> staffMenberships = memberships.stream().filter(member -> "STAFF".equals(member.getRole())).toList();

        model.addAttribute("memberships", staffMenberships);

        return "staff/eventMG";
    }

    @GetMapping ("/staff/booths/{boothId}/manage")
    public String manageBooth(@PathVariable Long boothId, Authentication authentication, Model model){
        String email = authentication.getName();

        List<EventMembers> memberships = eventMemberRepository.findByUser_Email(email);

        EventMembers membership = memberships.stream()
                               .filter(member -> "STAFF".equals(member.getRole()))
                               .filter(member -> member.getBooth() != null)
                               .filter(member -> member.getBooth().getId().equals(boothId))
                               .findFirst()
                               .orElseThrow(()-> new AccessDeniedException("アクセス権限がありません"));

        model.addAttribute("membership", membership);
        model.addAttribute("booth", membership.getBooth());
        model.addAttribute("event", membership.getEvent());

        return "staff/manage";
    }


    
}

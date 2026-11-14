package com.event.event_manager.Controller;


import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Repository.EventMemberRepository;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class StaffEventManageController {

    private final EventMemberRepository eventMemberRepository;

    @GetMapping ("/staff/eventMG")
    public String selectStaffBooth(Long eventId,Authentication authentication, Model model){

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

        long staffCount = eventMemberRepository.countByEvent_IdAndBooth_IdAndRole(membership.getEvent().getId(), membership.getBooth().getId(), "STAFF");



        model.addAttribute("staffCount", staffCount);
        model.addAttribute("membership", membership);
        model.addAttribute("booth", membership.getBooth());
        model.addAttribute("event", membership.getEvent());

        return "staff/manage";
    }


    
}

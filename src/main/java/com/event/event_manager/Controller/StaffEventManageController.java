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

import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Events;
import com.event.event_manager.Repository.BoothsRepository;
import com.event.event_manager.Repository.EventMemberRepository;

import lombok.RequiredArgsConstructor;

@Controller 
@RequiredArgsConstructor 
public class StaffEventManageController {

    private final EventMemberRepository eventMemberRepository;
    private final BoothsRepository boothsRepository;
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

        EventMembers adminMembership = memberships.stream()
                        .filter(member -> "ADMIN".equals(member.getRole()))
                        .filter(member -> member.getEvent() != null)
                        .findFirst()
                        .orElse(null);

        EventMembers staffMembership = memberships.stream()
                        .filter(member -> "STAFF".equals(member.getRole()))
                        .filter(member -> member.getBooth() != null)
                        .filter(member -> member.getBooth().getId().equals(boothId))
                        .findFirst()
                        .orElse(null);
        
        if (adminMembership == null && staffMembership == null) {
            throw new AccessDeniedException("アクセス権限がありません");
            
        }

        Booths booth;
        Events event;

        if(adminMembership != null){
             booth = boothsRepository.findByIdAndEventId(boothId, adminMembership.getEvent().getId()).orElseThrow(()-> new AccessDeniedException("アクセス権限がありません。"));
             event = adminMembership.getEvent();

        }else if (staffMembership != null) {


            booth = staffMembership.getBooth();
            event = staffMembership.getEvent();
        }else{

            throw new AccessDeniedException("アクセス権限がありません。");
        }

        long staffCount = eventMemberRepository.countByEvent_IdAndBooth_IdAndRole(event.getId(),booth.getId() ,"STAFF");

        model.addAttribute("staffCount", staffCount);
        model.addAttribute("booth", booth);
        model.addAttribute("event", event);

        return "staff/manage";
    }


    
}

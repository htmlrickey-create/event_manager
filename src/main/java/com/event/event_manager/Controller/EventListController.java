package com.event.event_manager.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.event.event_manager.Entity.Users;
import com.event.event_manager.Repository.UsersRepository;
import com.event.event_manager.Service.EventService;

@Controller 
@RequiredArgsConstructor 
public class EventListController {

    private final EventService service;
    private final UsersRepository usersRepository;


    @GetMapping ("/MyEvent")
    public String viewMyEventsPage(Model model ,Authentication authentication){

        String email = authentication.getName();

        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new IllegalStateException("ユーザーが見つかりません"));

        model.addAttribute("events", service.getAdminEvent());

        return "MyEvent";
    }

    @PostMapping ("/event/delete")
    public String DeleteEvent(@RequestParam Long eventId){

        service.deleteEvent(eventId);

        return "redirect:/MyEvent";

    }
}

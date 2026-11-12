package com.event.event_manager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.event.event_manager.Service.EventService;

@Controller 
@RequiredArgsConstructor 
public class EventListController {

    private final EventService service;


    @GetMapping ("/MyEvent")
    public String viewMyEventsPage(Model model){

        model.addAttribute("events", service.getAdminEvent());

        return "MyEvent";
    }

    @PostMapping ("/event/delete")
    public String DeleteEvent(@RequestParam Long eventId){

        service.deleteEvent(eventId);

        return "redirect:/MyEvent";

    }
}

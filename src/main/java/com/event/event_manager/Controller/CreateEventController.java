package com.event.event_manager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import com.event.event_manager.Form.EventForm;
import com.event.event_manager.Service.EventService;

@Controller 
@RequiredArgsConstructor 
public class CreateEventController {

    private final EventService service;

    @GetMapping ("/create_event")
    public String ViewCreateEventPage(Model model){

        model.addAttribute("eventForm", new EventForm());

        return "create_event";

    }

    @PostMapping ("/event/create")
    public String createEvent(@ModelAttribute ("eventForm") EventForm form , RedirectAttributes redirectAttributes){

        service.createEvent(form);

        redirectAttributes.addFlashAttribute("succesMsg", "イベントの作成が完了しました");
        return "redirect:/MyEvent";
    }
    
}

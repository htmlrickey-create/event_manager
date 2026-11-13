package com.event.event_manager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class NotEventController {

    @GetMapping ("notEvent")
    public String view(){
        return "notEvent";
    }
    
}

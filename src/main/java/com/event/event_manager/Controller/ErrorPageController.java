package com.event.event_manager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class ErrorPageController {

    @GetMapping ("/error/401")
    public String unauthorized(){
        return "error/401";
    }
    
}

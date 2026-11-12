package com.event.event_manager.Form;
import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class EventForm {

    private String schoolName;

    private String eventName;
    
    private String description;

    private String status;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
    
}

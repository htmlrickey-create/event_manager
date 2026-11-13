package com.event.event_manager.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;
@Entity 
@Table (name = "categories")
@Data 
public class Category {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "event_id", nullable = false)
    private Long eventId;

    @Column (name = "category_name", nullable = false)
    private  String categoryName;

    @Column (name = "display_order")
    private Integer displayOrder = 0;

    @Column (name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist 
    protected void onCreate(){
        if(displayOrder == null){
            displayOrder = 0;
        }

        if(createdAt == null){
            createdAt = LocalDateTime.now();
        }
    }
    
}

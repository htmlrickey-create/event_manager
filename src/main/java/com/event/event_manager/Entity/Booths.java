package com.event.event_manager.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity 
@Table (name = "booths")
@Data 
public class Booths {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column (name = "eventId", nullable = false)
    private Long eventId;

    @Column (name = "category_id", nullable = false)
    private Long categoryId;

    @Column (name = "booth_name", nullable = false , length = 150)
    private String boothName;

    private String description;

    private String location;

    @Column (name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    @Column (name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    
    @PrePersist 
    public void onCreate(){
        OffsetDateTime now = OffsetDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate 
    public void onUpdate(){
        this.updatedAt = OffsetDateTime.now();
    }
}

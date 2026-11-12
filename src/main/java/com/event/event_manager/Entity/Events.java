package com.event.event_manager.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import java.time.OffsetDateTime;
import lombok.Data;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
@Table (name = "events") 
@Data 
public class Events {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch =  FetchType.LAZY)
    @JoinColumn (name = "school_id", nullable = false)
    private SchoolGet school;

    @Column (name = "event_name", nullable = false, length = 200)
    private String eventName;

    @Column (name = "description", nullable = false, length = 500)
    private String description;

    @Column (name = "start_at")
    private OffsetDateTime startAt;

    @Column (name = "end_at")
    private OffsetDateTime endAt;

    @Column (name = "status", nullable = false, length = 50)
    private String status;

    @Column (name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column (name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist 
    protected void onCreate(){
        OffsetDateTime now = OffsetDateTime.now();
        
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = OffsetDateTime.now();
    }




    
}

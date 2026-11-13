package com.event.event_manager.Entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;

@Entity 
@Table  (name = "event_members")
@Data 
public class EventMembers {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "user_id", nullable = false)
    private Users user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "event_id", nullable = false)
    private Events event;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booth_id")
    private Booths booth;

    @Column (name = "role", nullable = false, length = 30)
    private String role;

    @Column (name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;

    @PrePersist 
    protected void onCreate(){
        this.joinedAt = OffsetDateTime.now();
    }
}

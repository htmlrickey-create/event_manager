package com.event.event_manager.Entity;

import lombok.Data;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity 
@Table (name = "products")
@Data 
public class Products {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "booth_id", nullable = false)
    private Booths booth;
    
    @Column (name = "product_name", nullable = false, length = 150)
    private String productName;

    @Column (name = "description")
    private String description;

    @Column (name = "price", nullable = false)
    private Integer price;

    @Column (name = "stock_quantity")
    private Integer stockQuantity;

    @Column (name =  "is_available", nullable = false)
    private Boolean isAvailable;

    @Column (name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column (name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist 
    public void onCreate(){
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate 
    public void onUpdate(){
        updatedAt = OffsetDateTime.now();
    }
}

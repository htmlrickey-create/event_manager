package com.event.event_manager.Form;

import lombok.Data;

@Data 
public class ProductForm {

    private String productName;

    private String description;

    private Integer price;

    private Integer stockQuantity;
    
}

package com.event.event_manager.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.event_manager.Entity.Products;

@Repository 
public interface ProductRepository extends JpaRepository<Products, Long> {  

    List<Products> findByBooth_Id(Long boothId);
    Optional<Products> findByIdAndBooth_Id(Long productId, Long boothId);
} 
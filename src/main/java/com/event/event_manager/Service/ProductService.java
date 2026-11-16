package com.event.event_manager.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.event.event_manager.Entity.Booths;
import com.event.event_manager.Entity.EventMembers;
import com.event.event_manager.Entity.Products;
import com.event.event_manager.Repository.BoothsRepository;
import com.event.event_manager.Repository.EventMemberRepository;
import com.event.event_manager.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProductService {

    private final ProductRepository productRepository;
    private final EventMemberRepository eventMemberRepository;
    private final BoothsRepository boothsRepository;

    public void checkBoothPermission(String email, Long boothId){

        List <EventMembers> memberships = eventMemberRepository.findByUser_Email(email);

        boolean isAdmin = memberships.stream()
                .filter(member -> "ADMIN".equals(member.getRole()))
                .filter(member -> member.getEvent() != null)
                .anyMatch(member -> boothsRepository.findByIdAndEventId(boothId, member.getEvent().getId()).isPresent());

        boolean isStaff = memberships.stream()
                .filter(member -> "STAFF".equals(member.getRole()))
                .filter(member -> member.getBooth() != null)
                .anyMatch(member -> member.getBooth().getId().equals(boothId));

        if (!isAdmin && !isStaff) {
            throw new AccessDeniedException("アクセス権限がありません");
        }
    }

    public List<Products> getProductsByBooth(String email, Long boothId){
        checkBoothPermission(email, boothId);

        return productRepository.findByBooth_Id(boothId);
    }

    public Products createProduct(String email, Long boothId, Products products){

        checkBoothPermission(email, boothId);

        Booths booth = boothsRepository.findById(boothId).orElseThrow(()-> new IllegalArgumentException("出店店舗が見つかりません"));

        products.setBooth(booth);

        return productRepository.save(products);
    }
    
}

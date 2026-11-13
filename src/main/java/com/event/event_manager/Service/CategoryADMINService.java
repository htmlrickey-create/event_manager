package com.event.event_manager.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.event.event_manager.Entity.Category;
import com.event.event_manager.Repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CategoryADMINService {

    private final CategoryRepository repository;

    public Category createCategory(Category category){
        return repository.save(category);
    }

    public List<Category> getCategoriesByEventId(Long eventId){
        return repository.findByEventId(eventId);
    }
    
}

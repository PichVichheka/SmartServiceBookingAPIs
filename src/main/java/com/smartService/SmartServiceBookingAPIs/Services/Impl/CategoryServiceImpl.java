package com.smartService.SmartServiceBookingAPIs.Services.Impl;


import com.smartService.SmartServiceBookingAPIs.Entity.Category;
import com.smartService.SmartServiceBookingAPIs.Repositories.CategoryRepository;
import com.smartService.SmartServiceBookingAPIs.Services.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }
}

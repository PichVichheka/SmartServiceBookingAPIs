package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.Entity.Category;
import com.smartService.SmartServiceBookingAPIs.Repositories.CategoryRepository;
import com.smartService.SmartServiceBookingAPIs.Services.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // ✅ CREATE
    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    // ✅ READ (ALL)
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ READ (BY ID)
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    // ✅ UPDATE
    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);

        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());

        return categoryRepository.save(existingCategory);
    }

    // ✅ DELETE
    @Override
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}

package com.smartService.SmartServiceBookingAPIs.Services;


import com.smartService.SmartServiceBookingAPIs.Entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);

    // ✅ READ (ALL)
    List<Category> getAllCategories();

    // ✅ READ (BY ID)
    Category getCategoryById(Long id);

    // ✅ UPDATE
    Category updateCategory(Long id, Category category);

    // ✅ DELETE
    void deleteCategory(Long id);
}

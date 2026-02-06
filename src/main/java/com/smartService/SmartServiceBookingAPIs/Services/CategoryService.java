package com.smartService.SmartServiceBookingAPIs.Services;


import com.smartService.SmartServiceBookingAPIs.DTO.request.CategoryRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.CategoryResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Category;

import java.util.List;

public interface CategoryService {
    PaginatedResponse<CategoryResponse> getAllCategories(int page, int size);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}

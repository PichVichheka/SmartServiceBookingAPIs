package com.smartService.SmartServiceBookingAPIs.Services.Impl;

import com.smartService.SmartServiceBookingAPIs.DTO.request.CategoryRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.CategoryResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Entity.Category;
import com.smartService.SmartServiceBookingAPIs.Repositories.CategoryRepository;
import com.smartService.SmartServiceBookingAPIs.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public PaginatedResponse<CategoryResponse> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryResponse> categoryResponses = categoryPage.getContent()
                .stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setId(category.getId());
                    response.setName(category.getName());
                    response.setDescription(category.getDescription());

                    return response;
                }).toList();

        PaginatedResponse.PaginationMeta paginationMeta =
                new PaginatedResponse.PaginationMeta(
                        categoryPage.getNumber() + 1,
                        categoryPage.getSize(),
                        categoryPage.getTotalElements(),
                        categoryPage.getTotalPages(),
                        categoryPage.hasNext(),
                        categoryPage.hasPrevious()
                );

        return new PaginatedResponse<>(categoryResponses, paginationMeta);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        return toResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        return toResponse(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}

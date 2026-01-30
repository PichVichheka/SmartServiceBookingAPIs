package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.CategoryRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.ApiResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.CategoryResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PaginatedResponse<CategoryResponse> categories = categoryService.getAllCategories(page, size);
        ApiResponse<PaginatedResponse<CategoryResponse>> response = new ApiResponse<>(
                true,
                "Categories retrieved successfully",
                categories
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
                true,
                "Category retrieved successfully",
                category
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
                true,
                "Category created successfully",
                category
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse category = categoryService.updateCategory(id, request);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
                true,
                "Category updated successfully",
                category
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Category deleted successfully"
        );
        return ResponseEntity.ok(response);
    }
}
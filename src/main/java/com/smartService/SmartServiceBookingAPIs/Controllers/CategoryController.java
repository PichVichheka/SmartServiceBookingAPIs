package com.smartService.SmartServiceBookingAPIs.Controllers;

import com.smartService.SmartServiceBookingAPIs.DTO.request.CategoryRequest;
import com.smartService.SmartServiceBookingAPIs.DTO.response.CategoryResponse;
import com.smartService.SmartServiceBookingAPIs.DTO.response.PaginatedResponse;
import com.smartService.SmartServiceBookingAPIs.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse create(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    public PaginatedResponse<CategoryResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return categoryService.getAllCategories(page, size);
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

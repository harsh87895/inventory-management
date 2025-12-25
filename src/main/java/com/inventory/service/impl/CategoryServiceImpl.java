package com.inventory.service.impl;

import com.inventory.dto.request.CategoryRequest;
import com.inventory.dto.response.CategoryResponse;
import com.inventory.exception.ResourceNotFoundException;
import com.inventory.model.Category;
import com.inventory.repository.CategoryRepository;
import com.inventory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DataIntegrityViolationException("Category with name " + request.getName() + " already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        
        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        // Check if new name is already taken by another category
        if (!category.getName().equals(request.getName()) && 
            categoryRepository.existsByName(request.getName())) {
            throw new DataIntegrityViolationException("Category with name " + request.getName() + " already exists");
        }

        category.setName(request.getName());
        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(updatedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findByIdWithProducts(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return mapToCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
            .map(this::mapToCategoryResponse);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        try {
            categoryRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Cannot delete category. It has associated products.");
        }
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
            .id(category.getId())
            .name(category.getName())
            .productCount(category.getProducts().size())
            .build();
    }
} 
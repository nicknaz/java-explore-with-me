package com.ewm.ewmmainservice.category.service;

import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface AdminCategoryService {

    CategoryDto create(CategoryDto categoryDto);

    CategoryDto patch(Long catId, CategoryDto categoryDto);

    void delete(Long catId);
}

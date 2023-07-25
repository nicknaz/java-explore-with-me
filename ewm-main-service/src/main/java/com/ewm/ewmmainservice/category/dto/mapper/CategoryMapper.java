package com.ewm.ewmmainservice.category.dto.mapper;

import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import com.ewm.ewmmainservice.category.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}

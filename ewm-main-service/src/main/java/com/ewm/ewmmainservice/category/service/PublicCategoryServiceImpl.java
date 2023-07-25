package com.ewm.ewmmainservice.category.service;

import com.ewm.ewmmainservice.category.dto.mapper.CategoryMapper;
import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private CategoryRepositoryJPA categoryRepositoryJPA;

    @Autowired
    public PublicCategoryServiceImpl(CategoryRepositoryJPA categoryRepositoryJPA) {
        this.categoryRepositoryJPA = categoryRepositoryJPA;
    }

    @Override
    public List<CategoryDto> getCategoryList(Pageable page) {
        return categoryRepositoryJPA.findAll(page)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return CategoryMapper.toCategoryDto(categoryRepositoryJPA.findById(catId)
                .orElseThrow(() -> new NotFoundedException("Категория не найдена")));
    }
}

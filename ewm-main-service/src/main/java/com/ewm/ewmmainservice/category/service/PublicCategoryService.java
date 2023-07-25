package com.ewm.ewmmainservice.category.service;

import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicCategoryService {
    List<CategoryDto> getCategoryList(Pageable page);
    CategoryDto getCategory(Long catId);
}

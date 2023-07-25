package com.ewm.ewmmainservice.category.service;

import com.ewm.ewmmainservice.category.dto.mapper.CategoryMapper;
import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import com.ewm.ewmmainservice.category.model.Category;
import com.ewm.ewmmainservice.category.repository.CategoryRepositoryJPA;
import com.ewm.ewmmainservice.event.repository.EventRepositoryJPA;
import com.ewm.ewmmainservice.exception.ConflictException;
import com.ewm.ewmmainservice.exception.NotFoundedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private CategoryRepositoryJPA categoryRepositoryJPA;
    private EventRepositoryJPA eventRepositoryJPA;

    @Autowired
    public AdminCategoryServiceImpl(CategoryRepositoryJPA categoryRepositoryJPA,
                                    EventRepositoryJPA eventRepositoryJPA) {
        this.categoryRepositoryJPA = categoryRepositoryJPA;
        this.eventRepositoryJPA = eventRepositoryJPA;
    }


    @Override
    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        if (categoryRepositoryJPA.findCategoriesByName(categoryDto.getName()) != null) {
            throw new ConflictException("Категория с таким названием уже существует!");
        }
        return CategoryMapper.toCategoryDto(categoryRepositoryJPA.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto patch(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepositoryJPA.findById(catId)
                .orElseThrow(() -> new NotFoundedException("Категория не найдена"));
        Category categoryByName = categoryRepositoryJPA.findCategoriesByName(categoryDto.getName());
        if (categoryByName != null && !categoryByName.getId().equals(catId)) {
            throw new ConflictException("Категория с таким названием уже существует!");
        }
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        return CategoryMapper.toCategoryDto(categoryRepositoryJPA.save(category));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        if (eventRepositoryJPA.findEventsByCategory(categoryRepositoryJPA.findById(catId)
                .orElseThrow(() -> new NotFoundedException("Категория не найдена"))).size() != 0) {
            throw new ConflictException("Невозможно удалить категорию, к которой привязаны события");
        }
        categoryRepositoryJPA.deleteById(catId);
    }
}

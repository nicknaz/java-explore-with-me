package com.ewm.ewmmainservice.category.controller;

import com.ewm.ewmmainservice.category.dto.model.CategoryDto;
import com.ewm.ewmmainservice.category.service.AdminCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private AdminCategoryService categoryService;

    @Autowired
    public AdminCategoryController(AdminCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto result = categoryService.create(categoryDto);
        log.info("Create category with id = {}", result.getId());
        return result;
    }

    @PatchMapping("/{catId}")
    public CategoryDto patch(@PathVariable Long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto result = categoryService.patch(catId, categoryDto);
        log.info("Update category with id = {}", result.getId());
        return result;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        categoryService.delete(catId);
        log.info("Delete category with id = {}", catId);

    }
}

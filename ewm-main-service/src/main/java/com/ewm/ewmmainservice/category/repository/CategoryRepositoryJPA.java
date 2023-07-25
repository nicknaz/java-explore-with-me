package com.ewm.ewmmainservice.category.repository;

import com.ewm.ewmmainservice.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepositoryJPA extends JpaRepository<Category, Long> {
    public Category findCategoriesByName(String name);
}

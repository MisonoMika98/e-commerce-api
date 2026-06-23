package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        // get all categories
        var categories = categoryRepository.findAll();

        return categories;
    }

    public Category getById(int categoryId)
    {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public Category create(Category category)
    {
        // create a new category
        var newCategory = categoryRepository.save(category);

        return newCategory;
    }

    public Category update(int categoryId, Category category)
    {
        Category newCategory = categoryRepository.findById(categoryId).orElse(null);

        if (newCategory == null) return null;

        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());

        return categoryRepository.save(newCategory);
    }

    public void delete(int categoryId)
    {
        // delete category
        categoryRepository.deleteById(categoryId);
    }
}

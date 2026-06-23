package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryService categoryService;
    private ProductService productService;


    @Autowired
    public CategoriesController(CategoryService categoryService, ProductService productService)
    {this.categoryService = categoryService; this.productService = productService;}


    @GetMapping("")
    public ResponseEntity<List<Category>> getAll()
    {
        // find and return all categories
        var categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }


    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id)
    {
        Category category = categoryService.getById(id);

        if (category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");

        // get the category by id
        return category;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productService.listByCategoryId(categoryId);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category)
    {
        // insert the category and return it with status 201 Created
        var newCategory = categoryService.create(category);

        URI location = URI.create("/categories/" + category.getCategoryId());

        return ResponseEntity.created(location).body(newCategory);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("{id}")
    public Category updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        if (categoryService.getById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");

        return categoryService.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        if (categoryService.getById(id) == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found");

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

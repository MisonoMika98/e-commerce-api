package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Category> getById(@PathVariable int id)
    {
        // get the category by id
        var category =  categoryService.getById(id);
        return ResponseEntity.ok(category);
    }


    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return null;
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
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id and return the updated category (200 OK)
        categoryService.update(id, category);
        return ResponseEntity.noContent().build();
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id)
    {
        // delete the category by id and return status 204 No Content
        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

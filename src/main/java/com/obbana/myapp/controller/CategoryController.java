package com.obbana.myapp.controller;

import com.obbana.myapp.domain.Category;
import com.obbana.myapp.domain.Product;
import com.obbana.myapp.repos.CategoryRepo;
import com.obbana.myapp.repos.ProductRepo;
import com.obbana.myapp.service.FileStorageService;
import com.obbana.myapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/category")
    public String main(Map<String, Object> model, Authentication authentication) {
        Utils.isAdminToModel(model, authentication);
        Iterable<Category> categories = categoryRepo.findAll();
        for(Category category : categories) {
            Integer count = categoryRepo.getCountProductsById(category.getId());
            if(count != null)
                category.setCnt(count);
        }
        model.put("categories", categories);
        return "category";
    }

    @GetMapping("/categoryAction")
    @PreAuthorize("hasRole('ADMIN')")
    public String actionGet(@RequestParam String act, @RequestParam Integer id, Map<String, Object> model, Authentication authentication) {
        switch(act) {
            case "delete":
                Category category = categoryRepo.findCategoryById(id);
                Iterable<Product> products = productRepo.findProductsByCategory(category);
                for(Product product : products) {
                    if(!"NO_IMAGE.png".equals(product.getFullImage()))
                        fileStorageService.deleteFile(product.getFullImage());
                    if(!"NO_IMAGE.png".equals(product.getImage()))
                        fileStorageService.deleteFile(product.getImage());
                    productRepo.deleteById(product.getId());
                }
                categoryRepo.deleteById(id);
                return "redirect:/category";
            case "change":
                model.put("change", id);
                return main(model, authentication);
            default:
                return "redirect:/category";
        }
    }

    @PostMapping("/categoryAction")
    @PreAuthorize("hasRole('ADMIN')")
    public String actionSave(@RequestParam String act, @RequestParam Integer id, @RequestParam String title, @RequestParam String description, Map<String, Object> model, Authentication authentication) {
                Category categoryFromDb = categoryRepo.findByTitle(title);
                if (categoryFromDb != null && categoryFromDb.getId() != id) {
                    model.put("message", "Категория с данным названием уже существует!");
                    return actionGet(act, id, model, authentication);
                } else {
                    categoryFromDb = categoryRepo.findCategoryById(id);
                    categoryFromDb.setTitle(title);
                    categoryFromDb.setDescription(description);
                    categoryRepo.save(categoryFromDb);
                }
                return "redirect:/category";
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCategory(Map<String, Object> model, Authentication authentication) {
        return "addCategory";
    }

    @PostMapping("/addCategory")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCategoryPost(@RequestParam String title, @RequestParam String description, Map<String, Object> model, Authentication authentication) {
        Category categoryFromDb = categoryRepo.findByTitle(title);
        if (categoryFromDb != null) {
            model.put("message", "Данная категория уже существует");
        } else {
            Category category = new Category(title, description);
            categoryRepo.save(category);
            model.put("message", "Категория успешно создана");
        }
        return addCategory(model, authentication);
    }

    @PostMapping("/categoryFilter")
    public String filter(@RequestParam String filter, Map<String, Object> model, Authentication authentication) {
        Utils.isAdminToModel(model, authentication);
        Iterable<Category> categories;
        if (filter != null && !filter.isEmpty()) {
            categories = categoryRepo.findCategoriesByTitleContains(filter);
        } else {
            categories = categoryRepo.findAll();
        }
        model.put("categories", categories);
        return "category";
    }

}
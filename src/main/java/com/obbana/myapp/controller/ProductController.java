package com.obbana.myapp.controller;

import com.obbana.myapp.domain.*;
import com.obbana.myapp.repos.*;
import com.obbana.myapp.service.FileStorageService;
import com.obbana.myapp.utils.Utils;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ProductController {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductPagingRepo productPagingRepo;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/product")
    public String main(Map<String, Object> model, Authentication authentication) {
        Utils.isAdminToModel(model, authentication);
        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);
        return "product";
    }

    @PostMapping("/productTable")
    public String productTable(@RequestParam String sortType, @RequestParam String sort, @RequestParam String filter, @RequestParam Integer elOnPage, @RequestParam Integer page, @RequestParam Integer category, Map<String, Object> model, Authentication authentication) {
        Utils.isAdminToModel(model, authentication);
        Page page2;
        Category category1;
        page--;

        Sort sorting = new Sort(("ASK".equals(sortType)) ? Sort.Direction.ASC: Sort.Direction.DESC, sort);
        Pageable pageable = PageRequest.of(page, elOnPage, sorting);

        if(filter != null && !filter.isEmpty() && category > 0) {
            // Если есть и фильтр и выборка по категории
            category1 = categoryRepo.findCategoryById(category);
            page2 = productPagingRepo.findProductsByCategoryAndTitleContains(category1, filter, pageable);
        } else if (category > 0) {
            // Если указана только категория
            category1 = categoryRepo.findCategoryById(category);
            page2 = productPagingRepo.findProductsByCategory(category1, pageable);
        } else if (filter != null && !filter.isEmpty()) {
            // Если указан только фильтр
            page2 = productPagingRepo.findProductsByTitleContains(filter, pageable);
        } else {
            // Если показываем ВСЁ
            page2 = productPagingRepo.findAll(pageable);
        }

        if(page2.getTotalPages() != 0) {
            List<Product> products = page2.getContent();
            model.put("products", products);
            model.put("pages", page2.getTotalPages());
            model.put("totalElements", page2.getTotalElements());
            model.put("viewElements", products.size());
            model.put("currentPage", page2.getNumber() + 1);
        }
        return "productTable";
    }

    @GetMapping("/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProduct(@RequestParam Integer id, Map<String, Object> model, Authentication authentication) {
        Product productFromDb = productRepo.findProductById(id);
        Iterable<Category> categories = categoryRepo.findAll();
        if (productFromDb != null ) {
            model.put("product", productFromDb);
            model.put("categories", categories);
            return "editProduct";
        } else {
            return "redirect:/product";
        }
    }

    @PostMapping("/editProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProductPost(@RequestParam MultipartFile file, @RequestParam Integer id, @RequestParam Integer category, @RequestParam String title, @RequestParam String description, @RequestParam Integer quantity, Map<String, Object> model, Authentication authentication) {
        Product product = productRepo.findByTitle(title);
        if (product != null && id != product.getId()) {
            model.put("message", "Товар с данным названием уже существует");
        } else if((product = productRepo.findProductById(id)) == null) {
            return "redirect:/product";
        } else {
            product.setTitle(title);
            product.setDescription(description);
            Category category1 = categoryRepo.findCategoryById(category);
            product.setCategory(category1);
            product.setQuantity(quantity);

            // Если изображение прикреплено, удаляем старые и добавляем новые
            if(!file.isEmpty()) {
                if(!"NO_IMAGE.png".equals(product.getFullImage()))
                    fileStorageService.deleteFile(product.getFullImage());
                if(!"NO_IMAGE.png".equals(product.getImage()))
                    fileStorageService.deleteFile(product.getImage());
                String absolutePath = fileStorageService.storeFileWithName(file, product.getFullImage());
                try {
                    Thumbnails.of(absolutePath)
                            .size(100, 100)
                            .toFile(new File(absolutePath.replace(product.getFullImage(), product.getImage())));
                } catch (IOException e) {
                    // Видимо это вообще не картинка...
                }
            }

            productRepo.save(product);

            model.put("message", "Товар успешно изменен");
        }
        return editProduct(id, model, authentication);
    }

    @PostMapping("/deleteProduct")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String actionSave(@RequestParam Integer id, Map<String, Object> model, Authentication authentication) {
        Product productFromDb = productRepo.findProductById(id);
        if (productFromDb != null) {
            // Удаляем картинки
            if(!"NO_IMAGE.png".equals(productFromDb.getFullImage()))
                fileStorageService.deleteFile(productFromDb.getFullImage());
            if(!"NO_IMAGE.png".equals(productFromDb.getImage()))
                fileStorageService.deleteFile(productFromDb.getImage());
            productRepo.deleteById(id);
            return "OK";
        } else {
            return "ERROR";
        }
    }

    @GetMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(Map<String, Object> model, Authentication authentication) {
        Iterable<Category> categories = categoryRepo.findAll();
        model.put("categories", categories);
        return "addProduct";
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProductPost(@RequestParam MultipartFile file, @RequestParam Integer category, @RequestParam String title, @RequestParam String description, @RequestParam Integer quantity, Map<String, Object> model, Authentication authentication) {
        Product productFromDb = productRepo.findByTitle(title);
        if (productFromDb != null) {
            model.put("message", "Данный товар уже существует");
        } else {
            Category category1 = categoryRepo.findCategoryById(category);
            Product product = new Product(category1, "", "", title, description, quantity);
            product = productRepo.save(product);
            // Получаем сгенерированный ID и сохраняем картинку
            if(file != null && !file.isEmpty()) {
                String fileName = product.getId() + ".jpg";
                String previewFileName = product.getId() + "_preview.jpg";
                String absolutePath = fileStorageService.storeFileWithName(file, fileName);

                try {
                    Thumbnails.of(absolutePath)
                            .size(100, 100)
                            .toFile(new File(absolutePath.replace(fileName, previewFileName)));
                } catch (IOException e) {
                }

                product.setImage(previewFileName);
                product.setFullImage(fileName);
            } else {
                product.setImage("NO_IMAGE.png");
                product.setFullImage("NO_IMAGE.png");
            }

            productRepo.save(product);

            model.put("message", "Товар успешно создан");
        }
        return addProduct(model, authentication);
    }

}
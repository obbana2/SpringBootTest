package com.obbana.myapp.repos;

import com.obbana.myapp.domain.Category;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepo extends CrudRepository<Category, Integer> {
    //Category findById(int id);
    Category findByTitle(String title);
    Category findCategoryById(Integer id);
    List<Category> findCategoriesByTitleContains(String filter);
    @Query(value = "SELECT sum(quantity) from product where category = ?1", nativeQuery = true)
    Integer getCountProductsById(int category);
}
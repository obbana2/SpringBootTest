package com.obbana.myapp.repos;

import com.obbana.myapp.domain.Category;
import com.obbana.myapp.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {
    Product findProductById(Integer id);
    //List<Product> findProductsByTitleContains(String title);
    void deleteById(Integer id);
    Product findByTitle(String title);
    Integer countAllByCategory(Integer category);

    Iterable<Product> findProductsByCategory(Category category);
    Iterable<Product> findProductsByTitleContains(String title);
    Iterable<Product> findProductsByCategoryAndTitleContains(Category category, String title);
}

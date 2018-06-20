package com.obbana.myapp.repos;

import com.obbana.myapp.domain.Category;
import com.obbana.myapp.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductPagingRepo extends PagingAndSortingRepository<Product, Integer> {
    Product findProductById(Integer id);
    //List<Product> findProductsByTitleContains(String title);
    void deleteById(Integer id);
    Product findByTitle(String title);
    Integer countAllByCategory(Integer category);
    // Выборка с сортировкой
    Page findProductsByCategory(Category category, Pageable pageable);
    Page findProductsByTitleContains(String title, Pageable pageable);
    Page findProductsByCategoryAndTitleContains(Category category, String title, Pageable pageable);
}

package com.nikitarizh.testtask.repository;

import com.nikitarizh.testtask.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM products p " +
            "LEFT JOIN product_tag pt ON p.id = pt.product_id " +
            "LEFT JOIN tags t ON t.id = pt.tag_id " +
            "WHERE " +
            " (:description IS NULL OR p.description LIKE %:description%) " +
            "   AND " +
            " (COALESCE(:tagIds) IS NULL OR t.id IN (:tagIds))")
    Set<Product> search(@Param("description") String description, @Param("tagIds") List<Integer> tagIds);
}

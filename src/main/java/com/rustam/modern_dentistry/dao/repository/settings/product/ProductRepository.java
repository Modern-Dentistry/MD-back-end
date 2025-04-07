package com.rustam.modern_dentistry.dao.repository.settings.product;

import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dto.response.create.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("""
    SELECT new com.rustam.modern_dentistry.dto.response.create.ProductResponse(
        p.id,p.category.id,p.category.categoryName,p.productName,p.quantity,p.price,p.sumPrice
    )
    FROM Product p
""")
    List<ProductResponse> findAllWithCategory();

    @Query("""
    SELECT new com.rustam.modern_dentistry.dto.response.create.ProductResponse(
        p.id,p.category.id,p.category.categoryName,p.productName,p.quantity,p.price,p.sumPrice
    )
    FROM Product p
    where p.id =:id
""")
    Optional<ProductResponse> findByIdWithCategory(Long id);

    boolean existsByProductName(String productName);
}

package com.rustam.modern_dentistry.service.settings.product;

import com.rustam.modern_dentistry.dao.entity.settings.product.Category;
import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dao.repository.settings.product.ProductRepository;
import com.rustam.modern_dentistry.dto.request.create.ProductRequest;
import com.rustam.modern_dentistry.dto.request.update.ProductUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.ProductResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.ModelMapperConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    ProductCategoryService productCategoryService;
    ModelMapper modelMapper;

    public ProductResponse create(ProductRequest productRequest) {
        Category category = productCategoryService.findById(productRequest.getCategoryId());
        boolean existsByProductName = productRepository.existsByProductName(productRequest.getProductName());
        if (existsByProductName){
            throw new ExistsException("This product is usually already available.");
        }
        BigDecimal sum = productRequest.getPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
        Product product = Product.builder()
                .category(category)
                .productName(productRequest.getProductName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .sumPrice(sum)
                .build();
        productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    public List<ProductResponse> read() {
        return productRepository.findAllWithCategory();
    }

    public ProductResponse update(ProductUpdateRequest productUpdateRequest) {
        Product product = findById(productUpdateRequest.getId());
        long quantitySum = 0L;
        if (productUpdateRequest.getCategoryId() != null){
            Category category = productCategoryService.findById(productUpdateRequest.getCategoryId());
            product.setCategory(category);
        }
        if (productUpdateRequest.getProductName() != null && productUpdateRequest.getProductName().isEmpty()){
            product.setProductName(productUpdateRequest.getProductName());
        }
        if (productUpdateRequest.getQuantity() != null && productUpdateRequest.getQuantity() != 0 ){
            quantitySum = product.getQuantity() + productUpdateRequest.getQuantity();
            product.setQuantity(quantitySum);
        }
        if (productUpdateRequest.getPrice() != null && !productUpdateRequest.getPrice().equals(BigDecimal.ZERO)){
            BigDecimal sum = productUpdateRequest.getPrice().multiply(BigDecimal.valueOf(quantitySum));
            product.setPrice(sum);
        }else {
            BigDecimal resultValue = product.getPrice().multiply(BigDecimal.valueOf(quantitySum));
            product.setPrice(resultValue);
        }
        productRepository.save(product);
        return modelMapper.map(product, ProductResponse.class);
    }

    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such product found."));
    }

    public ProductResponse readById(Long id) {
        return productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new NotFoundException("No such product found."));
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such product found."));
        productRepository.delete(product);
    }
}

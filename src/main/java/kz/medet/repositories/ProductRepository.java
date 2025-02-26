package kz.medet.repositories;

import kz.medet.dto.ProductDto;

import java.util.Optional;

public interface ProductRepository {
    Optional<ProductDto> findProductById(Long id);
    void saveProduct(ProductDto productDto);
    ProductDto addProduct(String name, double price);
}

package kz.medet.repositories.impl;

import kz.medet.dto.ProductDto;
import kz.medet.repositories.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private final Map<Long, ProductDto> productMap = new HashMap<>();

    private Long init_Id = 1L;

    @Override
    public Optional<ProductDto> findProductById(Long id) {
        return Optional.of(productMap.get(id));
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        productMap.put(productDto.getId(), productDto);
    }

    @Override
    public ProductDto addProduct(String name, double price) {
        ProductDto productDto = ProductDto.builder()
                .id(init_Id)
                .name(name)
                .price(price)
                .build();
        productMap.put(init_Id, productDto);
        init_Id++;
        return productDto;
    }
}

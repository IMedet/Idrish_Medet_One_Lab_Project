package kz.medet.repositories.impl;

import kz.medet.dto.ProductDto;
import kz.medet.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS products " +
                "(id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), price DOUBLE)");
    }

    private ProductDto mapRowToProductDto(ResultSet rs, int rowNum) throws SQLException {
        return ProductDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .build();
    }

    @Override
    public Optional<ProductDto> findProductById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sql, this::mapRowToProductDto, id));
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, productDto.getName(), productDto.getPrice(), productDto.getId());
    }

    @Override
    public ProductDto addProduct(String name, double price) {
        String sql = "INSERT INTO products(name,price) VALUES(?,?)";
        jdbcTemplate.update(sql, name, price);

        String selectProductByNameSql = "SELECT * FROM products WHERE name = ?";
        return jdbcTemplate.queryForObject(selectProductByNameSql, this::mapRowToProductDto, name);
    }
}

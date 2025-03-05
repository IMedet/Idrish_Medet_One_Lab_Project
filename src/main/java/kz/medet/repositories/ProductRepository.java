package kz.medet.repositories;

import kz.medet.dto.ProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<ProductDto,Long> {
    boolean existsByName(String productName);
    Optional<ProductDto> findByName(String productName);
}

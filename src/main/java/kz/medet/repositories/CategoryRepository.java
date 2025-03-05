package kz.medet.repositories;

import kz.medet.dto.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDto, Long> {
    boolean existsByName(String name);
}

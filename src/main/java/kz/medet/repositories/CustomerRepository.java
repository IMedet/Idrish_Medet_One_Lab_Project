package kz.medet.repositories;

import kz.medet.dto.CustomerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDto, Long> {
}

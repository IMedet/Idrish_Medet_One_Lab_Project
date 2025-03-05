package kz.medet.repositories;

import kz.medet.dto.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<OrderDto,Long> {
    List<OrderDto> findAllByCustomerId(Long customerId);
}

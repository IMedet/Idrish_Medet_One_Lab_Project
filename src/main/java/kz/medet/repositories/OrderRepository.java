package kz.medet.repositories;

import kz.medet.dto.OrderDto;

import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDto> findOrderById(Long id);
    void saveOrder(OrderDto orderDto);
    OrderDto addOrder();
}

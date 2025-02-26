package kz.medet.repositories.impl;

import kz.medet.dto.OrderDto;
import kz.medet.repositories.OrderRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    private final Map<Long, OrderDto> orderMap = new HashMap<>();

    private Long init_Id = 1L;

    @Override
    public Optional<OrderDto> findOrderById(Long id) {
        return Optional.ofNullable(orderMap.get(id));
    }

    @Override
    public void saveOrder(OrderDto orderDto) {
        orderMap.put(orderDto.getId(), orderDto);
    }

    @Override
    public OrderDto addOrder() {
        OrderDto orderDto = OrderDto.builder()
                .id(init_Id)
                .timeCreated(Timestamp.from(Instant.now()))
                .build();
        orderMap.put(init_Id, orderDto);
        init_Id++;

        return orderDto;
    }
}

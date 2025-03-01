package kz.medet.repositories.impl;

import kz.medet.Main;
import kz.medet.dto.OrderDto;
import kz.medet.repositories.OrderRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS orders " +
                "(id BIGINT AUTO_INCREMENT PRIMARY KEY, time_created TIMESTAMP)");
    }

    private OrderDto mapRowToOrderDto(ResultSet rs, int rowNum) throws SQLException {
        return OrderDto.builder()
                .id(rs.getLong("id"))
                .timeCreated(rs.getTimestamp("time_created"))
                .build();
    }


    @Override
    public Optional<OrderDto> findOrderById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToOrderDto, id));
        }catch (EmptyResultDataAccessException e){
            LOGGER.warning(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void saveOrder(OrderDto orderDto) {
        String sql = "UPDATE orders SET time_created = ? WHERE id = ?";
        jdbcTemplate.update(sql, orderDto.getTimeCreated(), orderDto.getId());
    }

    @Override
    public OrderDto addOrder() {
        String sql = "INSERT INTO orders(time_created) VALUES(?)";
        jdbcTemplate.update(sql, Timestamp.from(Instant.now()));

        String selectedLastInsertedSql = "SELECT MAX(time_created) AS max_time_created FROM orders";
        Timestamp lastInsertedTime = jdbcTemplate.queryForObject(selectedLastInsertedSql, Timestamp.class);

        String selectOrderByCreateTimeSql = "SELECT * FROM orders WHERE time_created = ?";
        return jdbcTemplate.queryForObject(selectOrderByCreateTimeSql, this::mapRowToOrderDto, lastInsertedTime);
    }
}

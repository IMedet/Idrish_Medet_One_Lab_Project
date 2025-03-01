package kz.medet.repositories.impl;


import kz.medet.Main;
import kz.medet.dto.CustomerDto;
import kz.medet.repositories.CustomerRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS customers " +
                "(id BIGINT AUTO_INCREMENT PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255))");
    }

    private CustomerDto mapRowToCustomerDto(ResultSet rs, int rowNum) throws SQLException {
        return CustomerDto.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .build();
    }

    @Override
    public Optional<CustomerDto> findCustomerDtoById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToCustomerDto, id));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.warning(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void addCustomerDto(String firstName, String lastName) {
        String sql = "INSERT INTO customers(first_name, last_name) VALUES(?,?)";
        jdbcTemplate.update(sql, firstName, lastName);
    }

    @Override
    public List<CustomerDto> getAllCustomer() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, this::mapRowToCustomerDto);
    }

    @Override
    public void saveCustomer(CustomerDto customerDto) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, customerDto.getFirstName(), customerDto.getLastName(), customerDto.getId());
    }
}

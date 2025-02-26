package kz.medet.repositories;

import kz.medet.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<CustomerDto> findCustomerDtoById(Long id);
    void addCustomerDto(String firstName, String lastName);
    List<CustomerDto> getAllCustomer();
    void saveCustomer(CustomerDto customerDto);
}

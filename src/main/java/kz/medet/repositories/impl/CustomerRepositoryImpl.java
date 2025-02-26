package kz.medet.repositories.impl;


import kz.medet.dto.CustomerDto;
import kz.medet.repositories.CustomerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<Long, CustomerDto> customerMap = new HashMap<>();

    private Long init_Id = 1L;

    @Override
    public Optional<CustomerDto> findCustomerDtoById(Long id) {
        return Optional.ofNullable(customerMap.get(id));
    }

    @Override
    public void addCustomerDto(String firstName, String lastName) {
        CustomerDto customerDto = CustomerDto.builder()
                .id(init_Id)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        customerMap.put(init_Id, customerDto);
        init_Id++;
    }

    @Override
    public List<CustomerDto> getAllCustomer() {
        return customerMap.values().stream().toList();
    }

    @Override
    public void saveCustomer(CustomerDto customerDto) {
        customerMap.put(customerDto.getId(), customerDto);
    }
}

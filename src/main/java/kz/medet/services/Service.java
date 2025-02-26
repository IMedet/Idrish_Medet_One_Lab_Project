package kz.medet.services;

import kz.medet.dto.CustomerDto;
import kz.medet.dto.OrderDto;
import kz.medet.dto.ProductDto;
import kz.medet.exceptions.ResourceNotFoundException;
import kz.medet.repositories.CustomerRepository;
import kz.medet.repositories.OrderRepository;
import kz.medet.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Autowired
    public Service(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public void addCustomer(String firstName, String lastName) {
        customerRepository.addCustomerDto(firstName, lastName);
    }

    public void addOrderToCustomer(Long customerId) {
        CustomerDto customerDto = customerRepository.findCustomerDtoById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId", customerId));
        OrderDto orderDto = orderRepository.addOrder();
        customerDto.getOrders().add(orderDto);
        customerRepository.saveCustomer(customerDto);
    }

    public void addProductToOrder(Long orderId, String name, double price) {
        OrderDto orderDto = orderRepository.findOrderById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "OrderId", orderId));
        ProductDto productDto = productRepository.addProduct(name, price);
        orderDto.getProducts().add(productDto);
        orderRepository.saveOrder(orderDto);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.getAllCustomer();
    }

    public List<OrderDto> getAllOrdersOfCustomer(Long customerId) {
        CustomerDto customerDto = customerRepository.findCustomerDtoById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId", customerId));
        return customerDto.getOrders();
    }

    public List<ProductDto> getAllProductsOfOrder(Long orderId) {
        OrderDto orderDto = orderRepository.findOrderById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "OrderId", orderId));
        return orderDto.getProducts();
    }
}

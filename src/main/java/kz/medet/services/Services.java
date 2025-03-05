package kz.medet.services;

import kz.medet.dto.CategoryDto;
import kz.medet.dto.CustomerDto;
import kz.medet.dto.OrderDto;
import kz.medet.dto.ProductDto;
import kz.medet.exceptions.AlreadyExistException;
import kz.medet.exceptions.ResourceNotFoundException;
import kz.medet.repositories.CategoryRepository;
import kz.medet.repositories.CustomerRepository;
import kz.medet.repositories.OrderRepository;
import kz.medet.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class Services {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public Services(CustomerRepository customerRepository, OrderRepository orderRepository,
                    ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addProductToCategory(Long categoryId, String productName, double price) {
        CategoryDto categoryDto = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Optional<ProductDto> productDto = productRepository.findByName(productName);

        if (productDto.isPresent()) {
            throw new AlreadyExistException("Product", "productName", productName);
        }

        ProductDto productDto1 = new ProductDto();
        productDto1.setName(productName);
        productDto1.setPrice(price);

        productRepository.save(productDto1);
        categoryDto.getProducts().add(productDto1);
        categoryRepository.save(categoryDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCategory(String name) {
        boolean existsCategory = categoryRepository.existsByName(name);

        if (existsCategory) {
            throw new AlreadyExistException("Category", "categoryName", name);
        }

        categoryRepository.save(new CategoryDto(name));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomer(String firstName, String lastName) {
        CustomerDto customer = new CustomerDto();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customerRepository.save(customer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addOrderToCustomer(Long customerId) {
        CustomerDto customerDto = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId", customerId));
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomer(customerDto);
        orderRepository.save(orderDto);

        customerDto.getOrders().add(orderDto);
        customerRepository.save(customerDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addProductToOrder(Long orderId, String name, double price) {
        OrderDto orderDto = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "OrderId", orderId));
        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productRepository.save(productDto);

        orderDto.getProducts().add(productDto);
        orderRepository.save(orderDto);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Set<ProductDto> getProductsOfCategory(Long categoryId) {
        CategoryDto category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        return category.getProducts();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<OrderDto> getAllOrdersOfCustomer(Long customerId) {
        CustomerDto customerDto = customerRepository.findById(customerId).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "CustomerId", customerId));
        return customerDto.getOrders();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ProductDto> getAllProductsOfOrder(Long orderId) {
        OrderDto orderDto = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order", "OrderId", orderId));
        return orderDto.getProducts();
    }
}

package kz.medet.config;

import kz.medet.repositories.CustomerRepository;
import kz.medet.repositories.OrderRepository;
import kz.medet.repositories.ProductRepository;
import kz.medet.repositories.impl.CustomerRepositoryImpl;
import kz.medet.repositories.impl.OrderRepositoryImpl;
import kz.medet.repositories.impl.ProductRepositoryImpl;
import kz.medet.services.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CustomerRepository customerRepository() {
        return new CustomerRepositoryImpl();
    }

    @Bean
    public OrderRepository orderRepository() {
        return new OrderRepositoryImpl();
    }

    @Bean
    public ProductRepository productRepository() {
        return new ProductRepositoryImpl();
    }

    @Bean
    public Service service(CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           ProductRepository productRepository) {
        return new Service(customerRepository, orderRepository, productRepository);
    }
}


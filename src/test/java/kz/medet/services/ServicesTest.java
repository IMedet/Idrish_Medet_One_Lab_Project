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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicesTest {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    Services services;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddCustomer() {
        String fisrName = "Nicolas";
        String lastName = "Jackson";
        CustomerDto customer = new CustomerDto();

        when(customerRepository.save(any(CustomerDto.class))).thenReturn(customer);

        services.addCustomer(fisrName, lastName);

        verify(customerRepository, times(1)).save(any(CustomerDto.class));
    }

    @Test
    void testAddProductToCategory() {
        CategoryDto category = new CategoryDto();
        category.setId(1L);
        category.setProducts(new HashSet<>());
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        when(productRepository.findByName("productName")).thenReturn(Optional.empty());

        ProductDto product = new ProductDto();
        product.setName("productName");
        product.setPrice(0d);
        when(productRepository.save(any(ProductDto.class))).thenReturn(product);

        services.addProductToCategory(1L, "productName", 0d);

        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findByName("productName");
        verify(productRepository, times(1)).save(any(ProductDto.class));

        assertFalse(category.getProducts().contains(product));
    }

    @Test
    void addProductToCategoryThrowExceptionWhenCategoryNotFound() {
        Long categoryId = 1L;
        String productName = "New Product";
        double price = 200.0;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> services.addProductToCategory(categoryId, productName, price));

        verify(productRepository, never()).save(any(ProductDto.class));
        verify(categoryRepository, never()).save(any(CategoryDto.class));
    }

    @Test
    void addProductToCategoryThrowExceptionWhenProductAlreadyExists() {
        Long categoryId = 1L;
        String productName = "Existing Product";
        double price = 200.0;

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setProducts(new HashSet<>());

        ProductDto existingProduct = new ProductDto();
        existingProduct.setName(productName);
        existingProduct.setPrice(price);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryDto));
        when(productRepository.findByName(productName)).thenReturn(Optional.of(existingProduct));

        assertThrows(AlreadyExistException.class, () -> services.addProductToCategory(categoryId, productName, price));

        verify(productRepository, never()).save(any(ProductDto.class));
        verify(categoryRepository, never()).save(any(CategoryDto.class));
    }


    @Test
    void testAddCategoryWhenCategoryDoesNotExist() {
        when(categoryRepository.existsByName("name")).thenReturn(false);
        when(categoryRepository.save(any(CategoryDto.class))).thenReturn(new CategoryDto());

        services.addCategory("name");

        verify(categoryRepository, times(1)).existsByName("name");
        verify(categoryRepository, times(1)).save(any(CategoryDto.class));
    }

    @Test
    void testAddCategoryWhenCategoryAlreadyExists() {
        String categoryText = "Exising category";
        when(categoryRepository.existsByName(categoryText)).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> services.addCategory(categoryText));

        verify(categoryRepository, never()).save(any(CategoryDto.class));
    }

    @Test
    void testAddOrderToCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);
        customer.setOrders(new ArrayList<>());
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        OrderDto order = new OrderDto();
        order.setId(100L);
        when(orderRepository.save(any(OrderDto.class))).thenReturn(order);
        services.addOrderToCustomer(1L);

        verify(customerRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(OrderDto.class));

        assertFalse(customer.getOrders().contains(order));
    }

    @Test
    void testAddOrderToCustomerThrowsExceptionWhenCustomerAlreadyExists() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> services.addOrderToCustomer(1L));

        verify(customerRepository, never()).save(any(CustomerDto.class));
        verify(orderRepository, never()).save(any(OrderDto.class));
    }


    @Test
    void testAddProductToOrder() {
        Long orderId = 1L;
        String productName = "Test Product";
        double price = 100.0;

        OrderDto orderDto = new OrderDto();
        orderDto.setId(orderId);
        orderDto.setProducts(new ArrayList<>());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderDto));

        services.addProductToOrder(orderId, productName, price);

        ArgumentCaptor<ProductDto> productCaptor = ArgumentCaptor.forClass(ProductDto.class);
        verify(productRepository).save(productCaptor.capture());

        ProductDto savedProduct = productCaptor.getValue();
        assertEquals(productName, savedProduct.getName());
        assertEquals(price, savedProduct.getPrice());

        verify(orderRepository).save(orderDto);
        assertFalse(orderDto.getProducts().isEmpty());
    }

    @Test
    void addProductToOrderThrowExceptionWhenOrderNotFound() {
        Long orderId = 1L;
        String productName = "Test Product";
        double price = 100.0;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> services.addProductToOrder(orderId, productName, price));

        verify(productRepository, never()).save(any(ProductDto.class));
        verify(orderRepository, never()).save(any(OrderDto.class));
    }


    @Test
    void testGetAllCategories() {
        CustomerDto customerDto = new CustomerDto(1L, "firstName", "lastName", List.of());
        OrderDto orderDto = new OrderDto(1L, new Timestamp(0), customerDto, List.of());
        ProductDto productDto = new ProductDto(1L, "name", 0.0, "description", List.of(orderDto), null);
        CategoryDto categoryDto = new CategoryDto(1L, "name", Set.of(productDto));

        when(categoryRepository.findAll()).thenReturn(List.of(categoryDto));

        List<CategoryDto> result = services.getAllCategories();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(categoryDto.getId(), result.get(0).getId());
        Assertions.assertEquals(categoryDto.getName(), result.get(0).getName());
    }


    @Test
    void testGetProductsOfCategory() {
        CategoryDto categoryDto = new CategoryDto(1L, "name", Set.of(new ProductDto(1L, "name", 0.0, "description", List.of(), null)));

        when(categoryRepository.findById(eq(1L))).thenReturn(Optional.of(new CategoryDto()));
        when(productRepository.findAll()).thenReturn(List.of(new ProductDto()));

        Set<ProductDto> result = services.getProductsOfCategory(1L);
    }

    @Test
    void testShowAllCustomers() {
        CustomerDto customer1 = new CustomerDto();
        CustomerDto customer2 = new CustomerDto();

        customer1.setId(1L);
        customer1.setFirstName("Nicolas");
        customer1.setLastName("Jackson");

        customer2.setId(2L);
        customer2.setFirstName("Cole");
        customer2.setLastName("Palmer");

        List<CustomerDto> customers = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDto> result = services.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("Nicolas", result.get(0).getFirstName());
        assertEquals("Cole", result.get(1).getFirstName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetOrdersOfCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setId(1L);

        OrderDto order1 = new OrderDto();
        order1.setId(1L);
        order1.setCustomer(customer);

        OrderDto order2 = new OrderDto();
        order2.setId(2L);
        order2.setCustomer(customer);

        List<OrderDto> orders = Arrays.asList(order1, order2);

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(orderRepository.findAllByCustomerId(customer.getId())).thenReturn(orders);

        customer.setOrders(orders);
        List<OrderDto> result = services.getAllOrdersOfCustomer(customer.getId());

        assertEquals(2, result.size());
    }


    @Test
    void testGetOrdersOfCustomerThrowsNotFoundException() {
        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> services.getAllOrdersOfCustomer(anyLong()));
    }

    @Test
    void testGetProductsOfOrder() {
        OrderDto order = new OrderDto();
        order.setId(1L);

        List<ProductDto> products = new ArrayList<>();
        ProductDto product1 = new ProductDto();
        ProductDto product2 = new ProductDto();

        product1.setId(1L);
        product2.setId(2L);
        product1.setName("Product 1");
        product2.setName("Product 2");

        products.add(product1);
        products.add(product2);

        order.setProducts(products);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        List<ProductDto> result = services.getAllProductsOfOrder(order.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository).findById(eq(order.getId()));
    }

    @Test
    void testGetProductsOfOrderThrowsNotFoundException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> services.getAllProductsOfOrder(anyLong()));
    }

}


package kz.medet;

import kz.medet.config.AopConfig;
import kz.medet.exceptions.AlreadyExistException;
import kz.medet.exceptions.ResourceNotFoundException;
import kz.medet.services.Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        Services services = context.getBean(Services.class);


        Scanner in = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("--------\n" +
                    "What you wanna ?\n" +
                    "1 - Show All Customers\n" +
                    "2 - Add Customer\n" +
                    "3 - Add Order To Customer\n" +
                    "4 - Show Order of Customer\n" +
                    "5 - Add Product To Order\n" +
                    "6 - Show Product of Order\n" +
                    "7 - Show All Categories\n" +
                    "8 - Add Category\n" +
                    "9 - Show products of Category\n" +
                    "10 - Add product to Category\n" +
                    "11 - Exit\n\n");

            System.out.println("Enter your choice below: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Customers: \n");
                    services.getAllCustomers().stream().forEach(customerDto ->
                            System.out.println("Id: " + customerDto.getId() + ", "
                                    + "FirstName: " + customerDto.getFirstName() + ", "
                                    + "LastName: " + customerDto.getLastName()));
                    break;
                case 2:
                    in.nextLine();
                    System.out.println("Enter customer's firstName: ");
                    String firstName = in.nextLine();

                    System.out.println("Enter customer's lastName: ");
                    String lastName = in.nextLine();

                    services.addCustomer(firstName, lastName);

                    System.out.println("Customer successfully added");
                    break;
                case 3:
                    System.out.println("Enter Id which customer you want to add Order: ");
                    Long customerId = in.nextLong();

                    try {
                        services.addOrderToCustomer(customerId);
                    } catch (ResourceNotFoundException exception) {
                        LOGGER.warning(exception.getMessage());
                    }

                    System.out.println("Order was created for Customer Id: " + customerId);
                    break;
                case 4:
                    System.out.println("Enter Id of Customer whom orders you wanna see: ");
                    Long customer_Id = in.nextLong();
                    try {
                        services.getAllOrdersOfCustomer(customer_Id).stream().forEach(
                                orderDto -> System.out.println(orderDto.toString()));
                    } catch (ResourceNotFoundException exception) {
                        LOGGER.warning(exception.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Enter Order Id to add Product");
                    Long order_Id = in.nextLong();

                    in.nextLine();

                    System.out.println("Enter name of Product: ");
                    String name = in.nextLine();

                    System.out.println("Enter price of Product: ");
                    double price = in.nextDouble();

                    try {
                        services.addProductToOrder(order_Id, name, price);
                    } catch (ResourceNotFoundException exception) {
                        LOGGER.warning("ResourceNotFoundException: " + exception.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Enter Order Id to see its Product List: ");
                    Long orderId = in.nextLong();

                    try {
                        services.getAllProductsOfOrder(orderId).stream().forEach(
                                productDto -> System.out.println(productDto.toString()));
                    } catch (ResourceNotFoundException exception) {
                        LOGGER.warning(exception.getMessage());
                    }
                    break;
                case 7:
                    System.out.println("Categories: \n");
                    services.getAllCategories().stream().forEach(categoryDto ->
                            System.out.println("Id: " + categoryDto.getId() + ", "
                                    + "Category Name: " + categoryDto.getName()));
                    break;
                case 8:

                    in.nextLine();
                    System.out.println("Enter category name: ");
                    String categyName = in.nextLine();

                    try {
                        services.addCategory(categyName);
                    } catch (AlreadyExistException exception) {
                        LOGGER.warning(exception.getMessage());
                    }

                    System.out.println("Category successfully added");
                    break;
                case 9:
                    System.out.println("Enter category ID: ");
                    Long categyId = in.nextLong();

                    try {
                        services.getProductsOfCategory(categyId).stream().forEach(
                                (product) -> System.out.println(product.toString())
                        );
                    } catch (Exception exception) {
                        LOGGER.warning(exception.getMessage());
                    }
                    break;
                case 10:
                    System.out.println("Enter category ID: ");
                    Long categy_Id = in.nextLong();

                    in.nextLine();

                    System.out.println("Enter product name: ");
                    String productName = in.nextLine();

                    System.out.println("Enter product price: ");
                    double productPrice = in.nextDouble();

                    try {
                        services.addProductToCategory(categy_Id, productName, productPrice);
                    } catch (ResourceNotFoundException | AlreadyExistException exception) {
                        LOGGER.warning(exception.getMessage());
                    }
                    break;
                case 11:
                    System.out.println("Existing the App!");
                    running = false;
                    break;
                default:
                    System.out.println("No such choice, try again ");
            }
        }

        in.close();
        System.exit(0);
    }
}
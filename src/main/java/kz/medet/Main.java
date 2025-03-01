package kz.medet;

import kz.medet.config.AppConfig;
import kz.medet.exceptions.ResourceNotFoundException;
import kz.medet.services.Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Service service = context.getBean(Service.class);


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
                    "7 - Exit\n\n");

            System.out.println("Enter your choice below: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Customers: \n");
                    service.getAllCustomers().stream().forEach(customerDto ->
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

                    service.addCustomer(firstName, lastName);

                    System.out.println("Customer successfully added");
                    break;
                case 3:
                    System.out.println("Enter Id which customer you want to add Order: ");
                    Long customerId = in.nextLong();

                    try {
                        service.addOrderToCustomer(customerId);
                    }catch (ResourceNotFoundException exception){
                        LOGGER.warning(exception.getMessage());
                    }

                    System.out.println("Order was created for Customer Id: " + customerId);
                    break;
                case 4:
                    System.out.println("Enter Id of Customer whom orders you wanna see: ");
                    Long customer_Id = in.nextLong();
                    try {
                        service.getAllOrdersOfCustomer(customer_Id).stream().forEach(
                                orderDto -> System.out.println(orderDto.toString()));
                    }catch (ResourceNotFoundException  exception){
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
                        service.addProductToOrder(order_Id, name, price);
                    } catch (ResourceNotFoundException exception) {
                        LOGGER.warning("ResourceNotFoundException: " + exception.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Enter Order Id to see its Product List: ");
                    Long orderId = in.nextLong();

                    try {
                        service.getAllProductsOfOrder(orderId).stream().forEach(
                                productDto -> System.out.println(productDto.toString()));
                    }catch (ResourceNotFoundException exception){
                        LOGGER.warning(exception.getMessage());
                    }
                    break;
                case 7:
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
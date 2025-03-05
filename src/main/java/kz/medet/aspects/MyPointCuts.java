package kz.medet.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class MyPointCuts {

    @Pointcut("execution(void kz.medet.services.Services.addCustomer(..))")
    public void addCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Services.getAllCustomers())")
    public void getCustomersMethod(){}

    @Pointcut("execution(* kz.medet.services.Services.addOrderToCustomer(..))")
    public void addOrderToCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Services.addProductToOrder(..))")
    public void addProductToOrderMethod(){}

    @Pointcut("execution(* kz.medet.services.Services.getAllOrdersOfCustomer(..))")
    public void getOrderOfCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Services.getAllProductsOfOrder(..))")
    public void getProductOfOrderMethod(){}
}

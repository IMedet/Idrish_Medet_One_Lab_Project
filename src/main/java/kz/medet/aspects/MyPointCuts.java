package kz.medet.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class MyPointCuts {

    @Pointcut("execution(void kz.medet.services.Service.addCustomer(..))")
    public void addCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Service.getAllCustomers())")
    public void getCustomersMethod(){}

    @Pointcut("execution(* kz.medet.services.Service.addOrderToCustomer(..))")
    public void addOrderToCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Service.addProductToOrder(..))")
    public void addProductToOrderMethod(){}

    @Pointcut("execution(* kz.medet.services.Service.getAllOrdersOfCustomer(..))")
    public void getOrderOfCustomerMethod(){}

    @Pointcut("execution(* kz.medet.services.Service.getAllProductsOfOrder(..))")
    public void getProductOfOrderMethod(){}
}

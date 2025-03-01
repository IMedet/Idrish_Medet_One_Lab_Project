package kz.medet.aspects;

import kz.medet.dto.OrderDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Aspect
public class LoggingAspect {

    private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("kz.medet.aspects.MyPointCuts.addCustomerMethod()")
    public void beforeAddCustomerLoggingAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        logger.info("beforeAddCustomerLoggingAdvice: логирование перед добавлением customer " +
                "methodSignature.getName() = " + methodSignature.getName());
        System.out.println("-----------------------------------------------------");

        logger.info("beforeAddCustomerLoggingAdvice: логирование переданных параметров перед добавлением " +
                "customer ");

        Object[] args = joinPoint.getArgs();

        for (Object obj : args) {
            String param = (String) obj;

            logger.info("param value is " + param + " ");
        }

        System.out.println("-----------------------------------------------------");
    }

    @After("kz.medet.aspects.MyPointCuts.getCustomersMethod()")
    public void afterShowCustomersLoggingAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        logger.info("afterShowCustomersLoggingAdvice: логирование получение всех покупателей " +
                "methodSignature.getMethod() = " + methodSignature.getMethod());

        System.out.println("-----------------------------------------------------");
    }

    @Around("kz.medet.aspects.MyPointCuts.addOrderToCustomerMethod()")
    public void aroundAddOrderToCustomerLoggingAdvice(ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable {
        logger.info("aroundAddOrderToCustomerLoggingAdvice: пытаются добавить OrderDto " +
                "для Customer ");
        System.out.println("-----------------------------------------------------");

        try {
            proceedingJoinPoint.proceed();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @AfterReturning(value = "kz.medet.aspects.MyPointCuts.getOrderOfCustomerMethod()",
            returning = "orders")
    public void afterReturningGetOrderOfCustomer(List<OrderDto> orders) {
        logger.info("afterReturningGetOrderOfCustomer: логируем список Orders" + orders);
    }

    @AfterThrowing(value = "kz.medet.aspects.MyPointCuts.getProductOfOrderMethod()",
    throwing = "exception")
    public void afterThrowingGetProductsOfOrder(Throwable exception) {
        logger.warn("afterThrowingGetProductsOfOrder: " + "логируем выброс " +
                "исключения "+exception.getMessage());
    }
}

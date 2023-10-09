package be.intecbrussel.productapp.aspect;

import be.intecbrussel.productapp.model.dto.LoginRequest;
import be.intecbrussel.productapp.model.dto.LoginResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {

    @Before("execution(* be.intecbrussel.productapp.service.RegisterService.login(..))")
    public void beforeLoginAttempt(JoinPoint joinPoint) {
        System.out.println("ATTEMPTING TO LOG IN");
        System.out.println("EMAIL: " + ((LoginRequest) joinPoint.getArgs()[0]).getEmail());
        System.out.println("PASSWORD: " + ((LoginRequest) joinPoint.getArgs()[0]).getPassword());
    }

    @After("execution(* be.intecbrussel.productapp.service.*.*(..))")
    public void afterRegisterServiceMethods(JoinPoint joinPoint) {
        System.out.println("Method " + joinPoint.getSignature().getName() + " has ended");
    }

    @AfterReturning(
            pointcut = "execution(* be.intecbrussel.productapp.service.RegisterService.login(..))",
            returning = "methodResult"
    )
    public void afterReturningLoginResponse(JoinPoint joinPoint, LoginResponse methodResult) {
        System.out.println("Login Response: ");
        System.out.println(methodResult);
    }

    @AfterThrowing(
            pointcut = "execution(* be.intecbrussel.productapp.service.RegisterService.login(..))",
            throwing = "exception"
    )
    public void afterLoginThrowsException(JoinPoint joinPoint, Exception exception) {
        System.out.println("Oopsy, something went wrong");
        exception.printStackTrace();
    }
}

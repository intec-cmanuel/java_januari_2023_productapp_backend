package be.intecbrussel.productapp.aspect;

import be.intecbrussel.productapp.logger.FileLogger;
import be.intecbrussel.productapp.model.dto.LoginRequest;
import be.intecbrussel.productapp.model.dto.LoginResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoginAspect {
    private final FileLogger fileLogger;

    public LoginAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }


    @Before("execution(* be.intecbrussel.productapp.service.RegisterService.login(..))")
    public void beforeLoginAttempt(JoinPoint joinPoint) {
        fileLogger.log("Attempting to log in: " + ((LoginRequest) joinPoint.getArgs()[0]).getEmail());
    }

    @After("execution(* be.intecbrussel.productapp.service.RegisterService.login(..))")
    public void afterRegisterServiceMethods(JoinPoint joinPoint) {
        fileLogger.log("Log in attempt concluded: " + ((LoginRequest) joinPoint.getArgs()[0]).getEmail());
    }

    @AfterReturning(
            pointcut = "execution(* be.intecbrussel.productapp.service.RegisterService.login(..))",
            returning = "methodResult"
    )
    public void afterReturningLoginResponse(JoinPoint joinPoint, LoginResponse methodResult) {
        if (methodResult != null)
            fileLogger.log("Log in attempt successful: " + ((LoginRequest) joinPoint.getArgs()[0]).getEmail());
        else
            fileLogger.log("Log in attempt failed: No user found");
    }

    @AfterThrowing(
            pointcut = "execution(* be.intecbrussel.productapp.service.RegisterService.login(..))",
            throwing = "exception"
    )
    public void afterLoginThrowsException(JoinPoint joinPoint, Exception exception) {
        fileLogger.log("Log in attempt failed: " + ((LoginRequest) joinPoint.getArgs()[0]).getEmail());
        fileLogger.log("EXCEPTION: " + exception.getMessage());
        fileLogger.logException(exception);
    }
}

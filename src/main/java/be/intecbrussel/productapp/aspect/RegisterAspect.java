package be.intecbrussel.productapp.aspect;

import be.intecbrussel.productapp.logger.FileLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class RegisterAspect {
    private final FileLogger fileLogger;

    public RegisterAspect(FileLogger fileLogger) {
        this.fileLogger = fileLogger;
    }

    @Around("execution(* be.intecbrussel.productapp.service.RegisterService.createUser(..))")
    public void aroundRegister(ProceedingJoinPoint joinPoint) {
        // @Before
        String email = (String) joinPoint.getArgs()[0];
        fileLogger.log("Attempting registration: " + email);
        try {
            Object returnedObject = joinPoint.proceed(); // execute method
            // @After / @AfterReturning with returnedObject
            fileLogger.log("Registration complete: " + email);
        } catch (Throwable e) {
            // @AfterThrowing
            fileLogger.log("Registration failed: " + email);
            fileLogger.log("EXCEPTION: " + e.getMessage());
            fileLogger.logException(e);
        }
    }
}

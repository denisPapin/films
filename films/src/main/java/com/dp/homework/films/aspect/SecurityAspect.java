package com.dp.homework.films.aspect;

import com.dp.homework.films.annotation.MySecuredAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Aspect
@Component
public class SecurityAspect {

    @Pointcut("@annotation(com.dp.homework.films.annotation.MySecuredAnnotation)")
    public void secureAnnotation() {
    }

    @Around("secureAnnotation()")
    public Object beforeCallSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = joinPoint
                .getTarget()
                .getClass()
                .getMethod(
                        signature.getMethod().getName(),
                        signature.getMethod().getParameterTypes()
                );

        MySecuredAnnotation mySecuredAnnotation = method.getAnnotation(MySecuredAnnotation.class);
        List<String> rolesFromAnnotation = Arrays.asList(mySecuredAnnotation.value());
        List<String> userRoles =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();
        if(checkAuthority(rolesFromAnnotation, userRoles)) {
            throw new AccessDeniedException("Отказано в доступе");
        }
        return joinPoint.proceed();
    }

    private boolean checkAuthority(List<String> rolesFromAnnotation, List<String> userRoles) {
        return Collections.disjoint(rolesFromAnnotation, userRoles);
    }
}
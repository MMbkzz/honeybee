package com.stackstech.dcp.server.system.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

public class AnnotationUtil {

    public static <T extends Annotation> T getAnnotation(ProceedingJoinPoint proceedingJoinPoint, Class<T> annotationClass) {
        MethodSignature sign = (MethodSignature) proceedingJoinPoint.getSignature();
        return sign.getMethod().getAnnotation(annotationClass);
    }

}

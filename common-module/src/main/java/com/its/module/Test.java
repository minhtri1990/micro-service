package com.its.module;


import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;


public class Test {
    @com.its.module.model.annotation.Test
    private String get() {
        return "123";
    }
    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        Reflections reflections = new Reflections("com.its.module", new MethodAnnotationsScanner());
        Set<Method> methods = reflections.getMethodsAnnotatedWith(com.its.module.model.annotation.Test.class);
        System.out.println(methods.iterator().next());
    }
}

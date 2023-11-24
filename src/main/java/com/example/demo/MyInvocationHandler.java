package com.example.demo;

import com.sun.jdi.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(MyInvocationHandler.class);
    private final static String INSTANCE_EXCEPTION = "object is not an instance of declaring class";

    private Object target;

    public MyInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();

        Object result = null;
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException e) {
            if (e.getMessage().equals(INSTANCE_EXCEPTION)) {
                log.info("Object instance error, proceeding with the custom query");
            } else {
                throw e;
            }
        }

        System.out.println(method.getName() + "executed in " + (System.nanoTime() - startTime) + " nanoseconds");
        log.info("{} executed in: {}", method.getName(), (System.nanoTime() - startTime));
        for (Annotation annotation : method.getAnnotations()) {
            log.info("Annotation: {}", annotation.toString());
        }

        return result;
    }
}

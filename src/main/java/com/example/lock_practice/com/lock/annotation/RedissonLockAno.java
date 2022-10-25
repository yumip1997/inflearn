package com.example.lock_practice.com.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLockAno {

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    long waitTime() default 100;

    long leaseTime() default 10;
}

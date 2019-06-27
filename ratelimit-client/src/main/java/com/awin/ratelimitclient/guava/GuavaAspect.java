package com.awin.ratelimitclient.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class GuavaAspect {

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Before("@annotation(com.awin.ratelimitclient.guava.GuavaRateLimit)")
    public void before(JoinPoint joinPoint) {
        rateLimiter.acquire(1);
        System.out.println("now run "+LocalDateTime.now());
    }

}

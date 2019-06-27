package com.awin.ratelimitclient.custom;

import static com.awin.ratelimitclient.custom.CustomAspect.Permit.ALLOWED;
import static com.awin.ratelimitclient.custom.CustomAspect.Permit.DENIED;
import static com.awin.ratelimitclient.custom.CustomAspect.Permit.RESET;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class CustomAspect {

    @Autowired
    private Repository repo;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, CustomRateLimit rateLimit) throws Throwable {
        String id = rateLimit.id();
        long refreshInterval = rateLimit.refreshInterval();
        int limit = rateLimit.limit();

        Invocation invocation = repo.get(id);
        LocalDateTime now = LocalDateTime.now();
        switch (tryAcquire(invocation.lastInvocation, now, limit, invocation.counter, refreshInterval)) {
            case RESET:
                System.out.println("RESET");
                repo.reset(id);
                repo.increment(id, now);
                return joinPoint.proceed();
            case ALLOWED:
                System.out.println("ALLOWED");
                repo.increment(id, now);
                return joinPoint.proceed();
            case DENIED:
                System.out.println("DENIED");
                throw new LimitExceedException();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public enum Permit {
        RESET,
        ALLOWED,
        DENIED
    }

    public static Permit tryAcquire(
            LocalDateTime lastInvocation,
            LocalDateTime now,
            int limit,
            int counter,
            long refreshInternal
    ) {
        System.out.println("lastInvocation "+lastInvocation+" now "+now);
        if (lastInvocation.isBefore(now.minus(refreshInternal, ChronoUnit.MILLIS))) {
            return RESET;
        } else {
            if (counter < limit) {
                return ALLOWED;
            }
        }

        return DENIED;
    }
}

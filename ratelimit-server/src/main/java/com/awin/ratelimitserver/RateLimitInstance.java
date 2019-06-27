package com.awin.ratelimitserver;


import com.google.common.util.concurrent.RateLimiter;


public class RateLimitInstance {

    public static final RateLimitInstance INSTANCE = new RateLimitInstance();
    private RateLimiter rateLimiter = RateLimiter.create(1);

    private RateLimitInstance() {

    }

    public RateLimiter rateLimiter() {
        return rateLimiter;
    }

}

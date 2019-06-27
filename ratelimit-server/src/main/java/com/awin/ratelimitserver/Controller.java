package com.awin.ratelimitserver;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping(path = "server-throttle")
    public ResponseEntity with() {
        boolean result = RateLimitInstance.INSTANCE.rateLimiter().tryAcquire();
        if (result) {
            return ResponseEntity.ok("{\"message\": \"ok\"}");
        } else {
            return ResponseEntity.status(429).build();
        }
    }

    @GetMapping(path = "client-throttle")
    public String without() {
        return "{\"message\": \"ok\"}";
    }
}

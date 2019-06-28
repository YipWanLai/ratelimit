package com.awin.ratelimitclient;

import com.awin.ratelimitclient.custom.CustomRateLimit;
import com.awin.ratelimitclient.custom.LimitExceedException;
import com.awin.ratelimitclient.guava.GuavaRateLimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;


@Service
public class TransactionPaymentIdLookupService {

    @Autowired
    private RestOperations rest;

    //    @Retryable( // TO-SHOW-CIRCUIT-BREAKER
//            value = HttpClientErrorException.class,
//            maxAttempts = 2,
//            backoff = @Backoff(delay = 500)
//    )
    @Retryable(
            value = HttpClientErrorException.class,
            backoff = @Backoff(delay = 3000)
    )
    public ResponseEntity<String> callWithServerThrottle() {
        ResponseEntity result = rest.getForEntity("http://localhost:8080/server-throttle", String.class);
        // TODO: save database
        // TODO: commit offset
        return result;
    }

    @Recover
    public ResponseEntity<String> recover(HttpClientErrorException e) {
        System.out.println("Turn on circuit breaker"); // https://github.com/spring-projects/spring-kafka/issues/938
        throw e;
    }

    @CustomRateLimit(id = "transaction-api", refreshInterval = 1000, limit = 3)
    @Retryable(
            include = LimitExceedException.class,
            backoff = @Backoff(delay = 1000)
    )
    public ResponseEntity<String> callWithCustomClientThrottle() {
        return rest.getForEntity("http://localhost:8080/client-throttle", String.class);
    }

    @Recover
    public ResponseEntity<String> recover(LimitExceedException e) {
        System.out.println("Turn on circuit breaker"); // https://github.com/spring-projects/spring-kafka/issues/938
        throw e;
    }

    @GuavaRateLimit
    public ResponseEntity<String> callWithGuavaClientThrottle() {
        return rest.getForEntity("http://localhost:8080/client-throttle", String.class);
    }
}

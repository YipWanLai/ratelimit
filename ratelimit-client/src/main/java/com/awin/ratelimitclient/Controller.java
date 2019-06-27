package com.awin.ratelimitclient;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @Autowired
    public TransactionPaymentIdLookupService service;

    @GetMapping(path = "/server-throttle/{count}")
    public ResponseEntity<String> serverThrottle(@PathVariable int count) {
        List<ResponseEntity<String>> entities = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            entities.add(service.callWithServerThrottle());
        }

        String result = "";
        for (ResponseEntity e : entities) {
            result += e.getStatusCode().toString() + ",";
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/custom-client-throttle/{count}")
    public ResponseEntity<String> customClientThrottle(@PathVariable int count) {
        List<ResponseEntity<String>> entities = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            ResponseEntity<String> e = service.callWithCustomClientThrottle();
            entities.add(e);
        }

        String result = "";
        for (ResponseEntity e : entities) {
            result += e.getStatusCode() + ",";
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/guava-client-throttle/{count}")
    public ResponseEntity<String> guavaClientThrottle(@PathVariable int count) {
        List<ResponseEntity<String>> entities = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            ResponseEntity<String> e = service.callWithGuavaClientThrottle();
            entities.add(e);
        }

        String result = "";
        for (ResponseEntity e : entities) {
            result += e.getStatusCode() + ",";
        }

        return ResponseEntity.ok(result);
    }

}

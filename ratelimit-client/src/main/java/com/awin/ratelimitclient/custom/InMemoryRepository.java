package com.awin.ratelimitclient.custom;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;


@Component
public class InMemoryRepository implements Repository {

    private Map<String, Invocation> storage = new HashMap<>();

    @Override
    public Invocation get(String id) {
        if (!storage.containsKey(id)) {
            storage.put(id, new Invocation());
        }

        return storage.get(id);
    }

    @Override
    public void increment(String id, LocalDateTime dateTime) {
        storage.get(id).counter++;
        storage.get(id).lastInvocation = dateTime;
    }

    @Override
    public void reset(String id) {
        storage.get(id).counter = 0;
    }
}

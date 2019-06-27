package com.awin.ratelimitclient.custom;

import java.time.LocalDateTime;


public interface Repository {

    Invocation get(String id);

    void increment(String id, LocalDateTime dateTime);

    void reset(String id);

}

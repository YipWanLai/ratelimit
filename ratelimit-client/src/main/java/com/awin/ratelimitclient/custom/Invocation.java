package com.awin.ratelimitclient.custom;

import java.time.LocalDateTime;


public class Invocation {

    public LocalDateTime lastInvocation = LocalDateTime.MIN;
    public int counter = 0;


}

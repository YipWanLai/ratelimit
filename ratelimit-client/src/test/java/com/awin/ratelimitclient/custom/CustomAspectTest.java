package com.awin.ratelimitclient.custom;

import static com.awin.ratelimitclient.custom.CustomAspect.Permit.ALLOWED;
import static com.awin.ratelimitclient.custom.CustomAspect.Permit.DENIED;
import static com.awin.ratelimitclient.custom.CustomAspect.Permit.RESET;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;


public class CustomAspectTest {

    @Test
    public void testAcquire() {
        int counter = 0;
        int limit = 3;
        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 0),
                        time(0, 1),
                        limit,
                        counter,
                        2000
                ),
                ALLOWED
        );
        counter = 1;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 1),
                        time(0, 2),
                        limit,
                        counter,
                        2000
                ),
                ALLOWED
        );
        counter = 2;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 2),
                        time(0, 2),
                        limit,
                        counter,
                        2000
                ),
                ALLOWED
        );
        counter = 3;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 2),
                        time(0, 2),
                        limit,
                        counter,
                        2000
                ),
                DENIED
        );
        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 2),
                        time(0, 3),
                        limit,
                        counter,
                        2000
                ),
                DENIED
        );
        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 2),
                        time(0, 4),
                        limit,
                        counter,
                        2000
                ),
                DENIED
        );

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 2),
                        time(0, 5),
                        limit,
                        counter,
                        2000
                ),
                RESET
        );
        counter = 1;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 5),
                        time(0, 5),
                        limit,
                        counter,
                        2000
                ),
                ALLOWED
        );
        counter = 2;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 5),
                        time(0, 5),
                        limit,
                        counter,
                        2000
                ),
                ALLOWED
        );
        counter = 3;

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 5),
                        time(0, 6),
                        limit,
                        counter,
                        2000
                ),
                DENIED
        );

        assertEquals(
                CustomAspect.tryAcquire(
                        time(0, 5),
                        time(0, 9),
                        limit,
                        counter,
                        2000
                ),
                RESET
        );
    }

    private LocalDateTime time(int minute, int second) {
        return LocalDateTime.of(2019, 1, 1, 14, minute, second);
    }

}

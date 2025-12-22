package com.csuni.pjp.client.support;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TimersRegistry {
    private Set<Timer> timers = new HashSet<Timer>();

    public void cancelAll() {
        for(Timer timer : timers) {
            timer.cancel();
            timer.purge();
        }
        timers.clear();
    }

    public void register(Timer timer) {
        timers.add(timer);
    }
}

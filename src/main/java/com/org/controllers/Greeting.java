package com.org.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Greeting {
    private final long id;
    private final String greeting;
    private static final Logger LOGGER= LoggerFactory.getLogger(Greeting.class);

    Greeting(long id, String greeeting) {
        this.id = id;
        this.greeting = greeeting;
        LOGGER.info("HI {} {}","nilesh",12);

    }

    public long getId() {
        return id;
    }

    public String getGreeting() {
        return greeting;
    }


}

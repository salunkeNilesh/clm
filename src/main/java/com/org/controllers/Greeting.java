package com.org.controllers;

public class Greeting {
    private final long id;
    private final String greeting;

    Greeting(long id, String greeeting) {
        this.id = id;
        this.greeting = greeeting;
    }

    public long getId() {
        return id;
    }

    public String getGreeting() {
        return greeting;
    }


}

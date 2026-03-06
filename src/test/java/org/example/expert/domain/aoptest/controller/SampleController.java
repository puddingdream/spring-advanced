package org.example.expert.domain.aoptest.controller;

public class SampleController {

    private final Runnable delegate;

    public SampleController(Runnable delegate) {
        this.delegate = delegate;
    }

    public void getSomething(long id) {
        delegate.run();
    }
}

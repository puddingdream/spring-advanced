package org.example.expert.domain.aoptest.controller;

public class SampleAdminController {

    private final Runnable delegate;

    public SampleAdminController(Runnable delegate) {
        this.delegate = delegate;
    }

    public void deleteSomething(long id) {
        delegate.run();
    }
}

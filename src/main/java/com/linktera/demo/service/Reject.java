package com.linktera.demo.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class Reject implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("REJECTED! "
                + execution.getVariable("payload"));
    }
}

package com.linktera.demo.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class Approve implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("APPROVED! "
                + execution.getVariable("employee"));
    }
}

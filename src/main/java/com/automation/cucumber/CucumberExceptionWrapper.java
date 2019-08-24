package com.automation.cucumber;

import cucumber.runtime.CucumberException;
import gherkin.events.PickleEvent;

public class CucumberExceptionWrapper implements PickleEventWrapper{
	
	private CucumberException exception;

    CucumberExceptionWrapper(CucumberException e) {
        this.exception = e;
    }

    @Override
    public PickleEvent getPickleEvent() {
        throw this.exception;
    }

}
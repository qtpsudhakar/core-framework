package com.automation.cucumber;

import gherkin.events.PickleEvent;

public class PickleEventWrapperImpl implements PickleEventWrapper {

	private final PickleEvent pickleEvent;

    public PickleEventWrapperImpl(PickleEvent pickleEvent) {
        this.pickleEvent = pickleEvent;
    }

    public PickleEvent getPickleEvent() {
        return pickleEvent;
    }

    @Override
    public String toString() {
        return "\"" + pickleEvent.pickle.getName() + "\"";
    }
    
}

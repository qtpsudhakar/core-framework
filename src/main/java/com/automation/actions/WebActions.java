package com.automation.actions;

import com.automation.engine.Engine;

public class WebActions {

	private Engine engine;

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine driverEngine) {
		this.engine = driverEngine;
	}

	public WebActions(Engine driverEngine) {
		this.engine = driverEngine;
	}

}

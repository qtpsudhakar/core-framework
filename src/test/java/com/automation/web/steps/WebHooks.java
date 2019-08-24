package com.automation.web.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class WebHooks {

	@Before
	public void beforeHook(Scenario scenario) {
		System.out.println("Web-BeforeHook");

	}

	@After
	public void afterHook(Scenario scenario) {
		System.out.println("Web-AfterHook");

	}

}

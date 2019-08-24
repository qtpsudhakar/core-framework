package com.automation.web.runner;

import com.automation.testbase.TestBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/web",
				glue = "com.automation.web.steps",
				monochrome = true, 
				strict = false, 
				dryRun = false)
public class WebTestRunner extends TestBase {}
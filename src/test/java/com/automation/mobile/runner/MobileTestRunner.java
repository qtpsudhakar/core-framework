package com.automation.mobile.runner;

import com.automation.testbase.TestBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features/mobile",
				glue = "com.automation.mobile.steps",
				monochrome = true, 
				strict = false, 
				dryRun = false)
public class MobileTestRunner extends TestBase{}

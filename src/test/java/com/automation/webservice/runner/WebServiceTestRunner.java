package com.automation.webservice.runner;

import com.automation.testbase.TestBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(monochrome = true, strict = false, dryRun = false)
public class WebServiceTestRunner extends TestBase {}
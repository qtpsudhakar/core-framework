package com.automation.testrunners;

import com.automation.testbase.TestBase;

import cucumber.api.CucumberOptions;

@CucumberOptions(monochrome = true, strict = false, dryRun = false)
public class WebTestRunner extends TestBase {}
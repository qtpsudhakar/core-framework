package com.automation.testbase;

import org.apache.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.automation.cucumber.CucumberFeatureWrapper;
import com.automation.cucumber.TestNGCucumberRunner;
import com.automation.engine.WebDriverEngine;
import com.automation.report.ExtentReportManager;
import com.aventstack.extentreports.ExtentReports;

@Listeners(TestBase.class)
public class TestBase implements IExecutionListener {

	private Logger logger = Logger.getLogger(TestBase.class);
	private TestNGCucumberRunner testNGCucumberRunner;
	private static ExtentReports report;

	@Override
	public void onExecutionStart() {
		annotationsToLogger("onExecutionStart");
	}

	@BeforeSuite()
	public void beforeSuite() {
		annotationsToLogger("BeforeSuite");
	}

	@BeforeClass(alwaysRun = true)
	public void setUpClass(ITestContext context) throws Exception {
		annotationsToLogger("BeforeClass");
		testNGCucumberRunner = new TestNGCucumberRunner(context, this.getClass());
		report = ExtentReportManager.initializeReport();
		ExtentReportManager.setExtentReports(report);
		WebDriverEngine.initializeDriver();
	}

	@BeforeTest()
	public void beforeTest() {
		annotationsToLogger("BeforeTest");

	}

	@BeforeMethod()
	public void beforeMethod() {
		annotationsToLogger("BeforeMethod");
	}

	@Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
	public void feature(CucumberFeatureWrapper cucumberFeature) {
		annotationsToLogger("Test");
		testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
	}

	@DataProvider
	public Object[][] features() {
		annotationsToLogger("DataProvider");
		return testNGCucumberRunner.provideFeatures();
	}

	@AfterMethod()
	public void afterMethod() {
		annotationsToLogger("AfterMethod");

	}

	@AfterTest()
	public void afterTest() {
		annotationsToLogger("AfterTest");

	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {
		annotationsToLogger("AfterClass");
		testNGCucumberRunner.finish();
		ExtentReportManager.flushReport(report);
		WebDriverEngine.tearDownDriver();
	}

	@AfterSuite()
	public void afterSuite() {
		annotationsToLogger("AfterSuite");
	}

	@Override
	public void onExecutionFinish() {
		annotationsToLogger("onExecutionFinish");

	}

	public void annotationsToLogger(String annotationName) {
		String message = "---> " + "@" + annotationName + "...Initialized !! ";
		logger.info(message);
		System.out.println(message);
	}

}

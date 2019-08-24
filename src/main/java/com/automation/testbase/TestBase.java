package com.automation.testbase;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
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
import com.automation.cucumber.PickleEventWrapper;
import com.automation.cucumber.PickleEventWrapperImpl;
import com.automation.cucumber.TestNgCucumberRunner;
import com.automation.engine.ConcurrentEngine;
import com.automation.reporting.ExtentManager;
import com.automation.reporting.ExtentTestManager;
import com.aventstack.extentreports.Status;

import cucumber.api.Scenario;

@Listeners(TestBase.class)
public class TestBase implements IExecutionListener {

	public static Properties props;
	private String propertiesFile = "Config.properties";
	private boolean remoteExecution;
	private static String extentReport;
	private static String reportPath;
	private String browser;
	private static String reportDirectoryName;
	private static String reportDirName;
	private TestNgCucumberRunner testNGCukesRunner;

	private static int totalSceanariosExecuted = 1;
	private static int numberOfIterationsCurrentSceanario = 1;
	private static String previousScenarioName = null;

	@Override
	public void onExecutionStart() {
		defaultOnExecutionStart();
	}

	public void defaultOnExecutionStart() {
		loadProperties();
		setUpExtent();
	}

	@BeforeSuite
	public void beforeSuite(ITestContext context) throws Exception {
	}

	@BeforeTest
	public void beforeTest(ITestContext context) throws Exception {

	}

	@BeforeClass
	public void beforeClass(ITestContext context) throws Exception {
		setRemoteExecution();
		testNGCukesRunner = new TestNgCucumberRunner(context, this.getClass());
	}

	@BeforeMethod
	public void beforeMethod(ITestContext context, Method method) throws Exception {
		try {
			engineSetUp(context);
		} catch (Exception e) {
			throw e;
		}
	}

	@Test(groups = "cucumber", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
	public void runScenario(PickleEventWrapper pickleWrapper, CucumberFeatureWrapper featureWrapper) throws Throwable {
		try {
			String featureFileTopTag = pickleWrapper.getPickleEvent().pickle.getTags().get(0).getName();
			String currentScenarioName = new PickleEventWrapperImpl(pickleWrapper.getPickleEvent()).toString();

			if (totalSceanariosExecuted == 1) {
				previousScenarioName = currentScenarioName;
				reportSetup(currentScenarioName);
				assignReportTag(featureFileTopTag);
			} else if (!currentScenarioName.equals(previousScenarioName)) {
				previousScenarioName = currentScenarioName;
				numberOfIterationsCurrentSceanario = 1;
				reportSetup(currentScenarioName);
				assignReportTag(featureFileTopTag);
			}

			reportNodeSetup("Iteration: " + numberOfIterationsCurrentSceanario);

			numberOfIterationsCurrentSceanario = numberOfIterationsCurrentSceanario + 1;
			totalSceanariosExecuted = totalSceanariosExecuted + 1;

			testNGCukesRunner.runScenario(pickleWrapper.getPickleEvent());

		} catch (Exception e) {
			numberOfIterationsCurrentSceanario = numberOfIterationsCurrentSceanario + 1;
			ExtentTestManager.getTest().fail(e);
			e.printStackTrace();
			throw e;
		}
	}

	@Test(groups = "cucumber", description = "Runs Cucumber Features", dataProvider = "features", enabled = false)
	public void runFeatures(PickleEventWrapper pickleWrapper) throws Throwable {
		try {
			testNGCukesRunner.runScenario(pickleWrapper.getPickleEvent());
		} catch (Exception e) {
			ExtentTestManager.getTest().fail(e);
			e.printStackTrace();
			throw e;
		}
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Throwable {
		driverTearDown();
		flushExtentManager();
	}

	@AfterClass
	public void afterClass() {
	}

	@AfterTest
	public final void afterTest() throws Exception {

	}

	@AfterSuite
	public final void afterSuite() throws Exception {

	}

	@DataProvider(parallel = true)
	public Object[][] scenarios() {
		return testNGCukesRunner.provideScenarios();
	}

	@DataProvider
	public Object[][] features() {
		return testNGCukesRunner.provideFeatures();
	}

	@Override
	public void onExecutionFinish() {
		System.out.println("Finished at " + new SimpleDateFormat("yyy_MM-dd HH:mm:ss").format(new Date()));
	}

	protected void engineSetUp(ITestContext context) throws Exception {
		setTestParameters(context);
		Capabilities caps = null;
		ConcurrentEngine.createEngine(caps, browser, remoteExecution);
	}

	public void setRemoteExecution() {
		if (props.getProperty("run.type", "local").equalsIgnoreCase("grid"))
			remoteExecution = true;
	}

	private void setTestParameters(ITestContext context) {
		browser = System.getProperty("browser");
		Map<String, String> allParameters = context.getCurrentXmlTest().getAllParameters();
		for (Map.Entry<String, String> entry : allParameters.entrySet()) {
			if (entry.getKey().equals("browser")) {
				browser = entry.getValue();
			}
		}
	}

	public void flushExtentManager() {
		if (extentReport.equalsIgnoreCase("true")) {
			if (ExtentManager.getInstance() != null)
				ExtentManager.getInstance().flush();
		}
	}

	public void driverTearDown() {
		ConcurrentEngine.destroyEngine();
	}

	public void testPass(Scenario scenario) {
		String methodName = scenario.getName();
		System.out.println(methodName + "()--Passed");
		if (extentReport.equalsIgnoreCase("true")) {
			ExtentTestManager.getTest().log(Status.PASS, "Test Passed");
		}
	}

	public void testSkip(Scenario scenario) {
		String methodName = scenario.getName();
		System.out.println(methodName + "()--Skipped");
		if (extentReport.equalsIgnoreCase("true")) {
			ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
		}
	}

	public void testFail(Scenario scenario) {
		String methodName = scenario.getName();
		System.out.println(methodName + "()--Failed");
		if (extentReport.equalsIgnoreCase("true")) {
			ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");
		}
	}

	public void loadProperties() {
		try {
			props = new Properties();
			InputStreamReader input = new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(propertiesFile));
			if (input != null)
				props.load(input);
		} catch (Exception e) {
			System.out.println("loadProperties..There was a problem loading properties file.");
		}
	}

	public void setUpExtent() {
		if (props.getProperty("extent.report").equals("true")) {
			reportDirectoryName = "";
			reportDirName = "";
			reportPath = new File(System.getProperty("user.dir")) + reportDirName;
			reportDirectoryName = "";
			File screenshotDirectory = new File(reportPath + reportDirectoryName);
			screenshotDirectory.mkdirs();
		}
	}

	public void reportSetup(String scenario) {
		if (extentReport.equalsIgnoreCase("true")) {
			ExtentManager.getInstance(reportDirectoryName);
			ExtentTestManager.createTest("Scenario: " + scenario + browser);
		}
	}

	public void reportNodeSetup(String nodeName) {
		if (extentReport.equalsIgnoreCase("true")) {
			ExtentTestManager.createNode(nodeName);
		}
	}

	public void assignReportTag(String featureFileTopTag) {
		ExtentTestManager.assignCategory(ExtentTestManager.getParentTest(), featureFileTopTag);
	}

	/*
	 * private Logger logger = Logger.getLogger(TestBase.class); private
	 * TestNgCucumberRunner testNGCucumberRunner; private static ExtentReports
	 * report;
	 * 
	 * @Override public void onExecutionStart() {
	 * annotationsToLogger("onExecutionStart"); }
	 * 
	 * @BeforeSuite() public void beforeSuite() {
	 * annotationsToLogger("BeforeSuite"); }
	 * 
	 * @BeforeClass(alwaysRun = true) public void setUpClass(ITestContext context)
	 * throws Exception { annotationsToLogger("BeforeClass"); testNGCucumberRunner =
	 * new TestNgCucumberRunner(context, this.getClass()); report =
	 * ExtentReportManager.initializeReport();
	 * ExtentReportManager.setExtentReports(report);
	 * WebDriverEngine.initializeDriver(); }
	 * 
	 * @BeforeTest() public void beforeTest() { annotationsToLogger("BeforeTest");
	 * 
	 * }
	 * 
	 * @BeforeMethod() public void beforeMethod() {
	 * annotationsToLogger("BeforeMethod"); }
	 * 
	 * @Test(groups = "cucumber", description = "Runs Cucumber Feature",
	 * dataProvider = "features") public void feature(CucumberFeatureWrapper
	 * cucumberFeature) { annotationsToLogger("Test");
	 * testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature()); }
	 * 
	 * @DataProvider public Object[][] features() {
	 * annotationsToLogger("DataProvider"); return
	 * testNGCucumberRunner.provideFeatures(); }
	 * 
	 * @AfterMethod() public void afterMethod() {
	 * annotationsToLogger("AfterMethod");
	 * 
	 * }
	 * 
	 * @AfterTest() public void afterTest() { annotationsToLogger("AfterTest");
	 * 
	 * }
	 * 
	 * @AfterClass(alwaysRun = true) public void tearDownClass() throws Exception {
	 * annotationsToLogger("AfterClass"); testNGCucumberRunner.finish();
	 * ExtentReportManager.flushReport(report); WebDriverEngine.tearDownDriver(); }
	 * 
	 * @AfterSuite() public void afterSuite() { annotationsToLogger("AfterSuite"); }
	 * 
	 * @Override public void onExecutionFinish() {
	 * annotationsToLogger("onExecutionFinish");
	 * 
	 * }
	 * 
	 * public void annotationsToLogger(String annotationName) { String message =
	 * "---> " + "@" + annotationName + "...Initialized !! "; logger.info(message);
	 * System.out.println(message); }
	 */

}

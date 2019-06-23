package com.automation.report;

import static com.automation.utility.Config.reportPath;
import static com.automation.utility.Config.reportScreenShotPath;
import static com.automation.utility.Config.reportsDocumentName;
import static com.automation.utility.Config.reportsName;
import static com.automation.utility.Config.runEnvironment;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.automation.engine.WebDriverEngine;
import com.automation.utility.Config;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

	private static ExtentHtmlReporter extentHtmlReporter;
	private static ExtentReports extentReports;
	private static ExtentTest extentParentTest;
	private static ExtentTest extentChildTest;
	private static String reportName = "automation-report.html";

	private static Logger logger = Logger.getLogger(ExtentReportManager.class);

	public static ExtentReports initializeReport() {
		try {
			extentHtmlReporter = new ExtentHtmlReporter(reportPath + reportName);
			extentReports = new ExtentReports();
			extentReports.attachReporter(extentHtmlReporter);
			extentReports.setSystemInfo("OS", System.getProperty("osname"));
			extentReports.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
			extentReports.setSystemInfo("Environment", runEnvironment);
			extentReports.setSystemInfo("User Name", System.getProperty("user.name"));
			extentHtmlReporter.config().setTheme(Theme.DARK);
			extentHtmlReporter.config().setDocumentTitle(reportsDocumentName);
			extentHtmlReporter.config().setReportName(reportsName);
		} catch (UnknownHostException e) {
			logger.error("---> initializeReport - Error occured " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		return extentReports;

	}
	
	public static void createTest(String testCaseName) {
		extentParentTest = extentReports.createTest(testCaseName);
	}
	
	public static void assignCategory(ExtentTest test, String category) {
		test.assignCategory(category);
	}

	public static void createNode(String nodeName) {
		extentChildTest = extentParentTest.createNode(nodeName);
	}

	public static ExtentTest getChildTest() {
		return extentChildTest;
	}

	public static ExtentTest getParentTest() {
		return extentParentTest;
	}

	public static ExtentReports getExtentReports() {
		return extentReports;
	}

	public static void setExtentReports(ExtentReports report) {
		extentReports = report;
	}

	public static void flushReport(ExtentReports extentReports) {
		extentReports.flush();
	}

	public static void reportPass(String message) {
		String path = "";
		if (!Config.takeScreenshotForPassStep) {
			extentChildTest.log(Status.PASS, message);
		} else {
			path = takeScreenshot(WebDriverEngine.driver, message);
			try {
				extentChildTest.log(Status.PASS, message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} catch (IOException e) {
				logger.error("---> reportPass - Error occured " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public static void reportFail(String message) {
		String path = "";
		if (Config.takeScreenshotForFailStep) {
			path = takeScreenshot(WebDriverEngine.driver, message);
			try {
				extentChildTest.log(Status.FAIL, message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			} catch (IOException e) {
				logger.error("---> reportFail - Error occured " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		} else {
			extentChildTest.log(Status.FAIL, MarkupHelper.createLabel(message, ExtentColor.RED));
		}
	}

	public static void reportInfo(String message) {
		extentChildTest.log(Status.INFO, MarkupHelper.createLabel(message, ExtentColor.ORANGE));
	}

	protected static String takeScreenshot(WebDriver driver, String step) {
		String sdf = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
		long currentTime = System.currentTimeMillis();
		String destination = "";
		try {
			File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			destination = reportScreenShotPath + "-" + sdf + "-" + currentTime + ".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination, false);
			logger.info(destination);
			logger.info("Screenshot taken on " + driver + "for step " + step);
		} catch (Throwable t) {
			logger.error("---> takeScreenshot - Error occured " + t.getLocalizedMessage());
		}
		return destination;
	}

}

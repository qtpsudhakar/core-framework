package com.automation.reporting;

import java.io.File;
import java.util.Properties;

import com.automation.testbase.TestBase;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentHtmlReporter htmlReporter;
	private static ExtentReports extent;
	private static Properties props;
	public static String reportPath, reportDirectoryName, reportFileName, applicationName;

	public synchronized static ExtentReports getInstance(String directoryName) {
		props = TestBase.props;
		reportFileName = "execution-report";
		applicationName = "Tab Bank";
		reportPath = new File(System.getProperty("user.dir")) + reportDirectoryName;
		if (extent == null) {
			htmlReporter = new ExtentHtmlReporter(reportPath + directoryName + reportFileName);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			extent.setSystemInfo("Application", applicationName);
			extent.setSystemInfo("Run Platform", "Winodws");
			extent.setSystemInfo("Environment", "QA");
			htmlReporter.config().setDocumentTitle(applicationName + " Execution Test Report");
			htmlReporter.config().setReportName(reportFileName);
			htmlReporter.config().setTheme(Theme.DARK);
			htmlReporter.config().enableTimeline(true);
		}
		return extent;
	}
	
	public synchronized static ExtentReports getInstance() {
		return extent;
	}
}

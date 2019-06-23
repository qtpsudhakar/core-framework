package com.automation.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	public static Properties prop;
	public static final String testProperties = "test.properties";
	public static final String webProperties = "web.properties";
	public static final String mobileProperties = "mobile.properties";

	public static final String curDir = System.getProperty("user.dir");
	public static final String fp = File.separator;
	public static final String propFilesPath = curDir + fp + "src" + fp + "main" + fp + "resources" + fp;

	public static final String runType = getProperty("run.type");
	public static final String runEnvironment = getProperty("run.environment");
	public static final String testBrowser = getProperty("test.browser");
	public static final String driverBinaryVersion = getBrowserWebDriverBinaryVersion();
	public static final String appURL = getAppWebURL();
	
	public static final String reportPath = curDir + getProperty("extent.report.path");
	public static final String reportScreenShotPath = curDir + getProperty("extent.report.screenshot.path");
	public static final String reportsName = getProperty("extent.report.name");
	public static final String reportsUsername = getProperty("extent.report.username");
	public static final String reportsDocumentName = getProperty("extent.report.document.name");
	public static final boolean takeScreenshotForPassStep = Boolean.valueOf(getProperty("screenshot.pass.steps"));
	public static final boolean takeScreenshotForFailStep = Boolean.valueOf(getProperty("screenshot.fail.steps"));

	public static String getProperty(String key) {
		prop = new Properties();
		FileInputStream file;
		try {
			file = new FileInputStream(propFilesPath + testProperties);
			try {
				prop.load(file);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static String getPropertyWeb(String key) {
		Properties prop = new Properties();
		FileInputStream file;
		try {
			if (runType.equalsIgnoreCase("web-browser")) {
				file = new FileInputStream(propFilesPath + webProperties);
				try {
					prop.load(file);
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static String getPropertyMobile(String key) {
		Properties prop = new Properties();
		FileInputStream file;
		try {
			if (runType.equalsIgnoreCase("physical-mobile-device") || runType.equalsIgnoreCase("cloud-mobile-device")) {
				file = new FileInputStream(propFilesPath + mobileProperties);
				try {
					prop.load(file);
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static String getAppWebURL() {
		String temp = null;
		switch (runEnvironment.toUpperCase()) {
		case ("QA"):
			temp = getPropertyWeb("qa.url");
			break;
		case ("SIT"):
			temp = getPropertyWeb("sit.url");
			break;
		case ("STAGE"):
			temp = getPropertyWeb("stage.url");
			break;
		case ("UAT"):
			temp = getPropertyWeb("uat.url");
			break;
		}
		return temp;
	}

	public static String getBrowserWebDriverBinaryVersion() {
		String temp = null;
		switch (testBrowser.toUpperCase()) {
		case ("CHROME"):
			temp = getPropertyWeb("chrome.driver.version");
			break;
		case ("FIREFOX"):
			temp = getPropertyWeb("firefox.driver.version");
			break;
		case ("IE"):
			temp = getPropertyWeb("ie.driver.version");
			break;
		case ("SAFARI"):
			temp = getPropertyWeb("safari.driver.version");
			break;
		case ("OPERA"):
			temp = getPropertyWeb("opera.driver.version");
			break;
		}
		return temp;
	}

}

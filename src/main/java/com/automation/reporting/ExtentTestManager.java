package com.automation.reporting;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Holder class for extent tests to render them synchronously in their own
 * threads
 * 
 * @author Bharat Pandey
 *
 */
public class ExtentTestManager {

	private static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	private static ExtentReports extent = ExtentManager.getInstance();
	private static ExtentTest parentTest;
	private static ExtentTest childTest;

	public static synchronized void removeTest() {
		extent.removeTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
	}

	public static synchronized ExtentTest createTest(String testName) {
		parentTest = extent.createTest(testName);
		return parentTest;
	}

	public static synchronized ExtentTest createNode(String nodeName) {
		childTest = parentTest.createNode(nodeName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), childTest);
		return childTest;
	}

	public static synchronized ExtentTest getParentTest() {
		return parentTest;
	}

	public static synchronized ExtentTest getTest() {
		ExtentTest extentTest = extentTestMap.get((int) (long) (Thread.currentThread().getId()));
		return extentTest;
	}

	public static synchronized void assignCategory(ExtentTest test, String categoryName) {
		test.assignCategory(categoryName);
	}

}

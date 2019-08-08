package com.automation.utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import com.automation.engine.WebDriverEngine;

public class SeleniumUtility extends WebDriverEngine {

	@FindBy(xpath = "//*[@id='test']")
	public WebElement element;
	// Action Class

	Actions actions = new Actions(driver);

	public void understandActions() {

		// left click
		actions.click(element).build().perform();

		// right click

		actions.contextClick().build().perform();

		// mouse over
		actions.moveToElement(element).build().perform();

		// mouse over and click
		actions.moveToElement(element).click().build().perform();

		// double click
		actions.doubleClick(element).build().perform();

		// drag and drop
		actions.dragAndDrop(element, element).build().perform();

		// Click and hold
		actions.clickAndHold(element).build().perform();

	}

	public void understandAlerts() {

		driver.switchTo().alert().accept();
		driver.switchTo().alert().dismiss();
		driver.switchTo().alert().getText();

		Alert alert = driver.switchTo().alert();
		alert.accept();
		alert.dismiss();
		alert.getText();
	}

	public void understandJavaScriptExecutor() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// clicking on element
		js.executeScript("arguments[0].click();", element);

		// ScrollPage
		js.executeScript("window.scrollBy(0,150)");
	}

	// Take Screenshot
	public void takeScreenshot(WebDriver driver, String filePath) throws IOException {
		
		//2 ways to capture screenshot
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String screenshotBase64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
		
		//STEP1: Convert web driver object to TakeScreenshot
		TakesScreenshot scrShot = ((TakesScreenshot) driver);

		//STEP2: Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		//STEP3: Move image file to new destination
		File DestFile = new File(filePath);

		//STEP4: Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);
		
		
	
		 

	}
}

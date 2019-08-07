package com.automation.utility;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
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
		
		//clicking on element
		js.executeScript("arguments[0].click();", element);
		
		//ScrollPage
		js.executeScript("window.scrollBy(0,150)");
	}
}

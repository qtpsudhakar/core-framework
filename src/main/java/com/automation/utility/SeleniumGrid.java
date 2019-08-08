package com.automation.utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumGrid {

	WebDriver driver;

	String appURL = "https://opensource-demo.orangehrmlive.com/";
	String nodeURLff = "http://172.30.121.177:5557/wd/hub";
	String nodeURLch = "http://172.30.121.177:5558/wd/hub";
	String nodeURLie = "http://172.30.121.177:5559/wd/hub";

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		SeleniumGrid seleniumGrid = new SeleniumGrid();

		seleniumGrid.browserSetupFireFox();

		seleniumGrid.browserSetupChrome();

		seleniumGrid.browserSetupIE();

	}

	public void browserSetupFireFox() throws MalformedURLException, InterruptedException {
		driver = new RemoteWebDriver(new URL(nodeURLff), DesiredCapabilities.firefox());
		login(driver);

	}

	public void login(WebDriver driver) throws InterruptedException {
		driver.get(appURL);
		driver.manage().window().maximize();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id='txtUsername']")).sendKeys("admin");
		driver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).sendKeys("admin123");
		driver.findElement(By.xpath("//*[@id=\"btnLogin\"]")).click();
		driver.quit();
	}

	public void browserSetupChrome() throws MalformedURLException, InterruptedException {
		driver = new RemoteWebDriver(new URL(nodeURLch), DesiredCapabilities.chrome());
		login(driver);
	}

	public void browserSetupIE() throws MalformedURLException, InterruptedException {
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability("requireWindowFocus", true);
		driver = new RemoteWebDriver(new URL(nodeURLie), capabilities);
		login(driver);
	}

}
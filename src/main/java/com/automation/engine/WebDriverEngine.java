package com.automation.engine;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.automation.utility.Config;

import static com.automation.utility.Config.*;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverEngine {

	static Logger logger = Logger.getLogger(WebDriverEngine.class);
	public static WebDriver driver;

	public static WebDriver initializeDriver() {
		if (testBrowser.equalsIgnoreCase("chrome")) {
			//WebDriverManager.chromedriver().arch64().version(driverBinaryVersion).setup();
			WebDriverManager.chromedriver().version(driverBinaryVersion).setup();
			driver = new ChromeDriver();
			logger.info(">>> Chrome Driver initialized");

		} else if (testBrowser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().arch64().version(driverBinaryVersion).setup();
			driver = new FirefoxDriver();
			logger.info(">>> Firefox Driver initialized");
		}

		driver.get(Config.appURL);
		driver.manage().window().maximize();
		
		return driver;
	}

	public static void tearDownDriver() {
		driver.close();
		driver.quit();
	}

}

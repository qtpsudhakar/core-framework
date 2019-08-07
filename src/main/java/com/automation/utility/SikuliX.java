package com.automation.utility;

import static com.automation.utility.Config.driverBinaryVersion;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SikuliX {

	public static void main(String args[]) throws FindFailed, InterruptedException {
		new SikuliX().clickLoginButtonSikuli();
	}

	public void clickLoginButtonSikuli() throws InterruptedException, FindFailed {

		Screen screen = new Screen();
		Pattern loginButton = new Pattern(
				System.getProperty("user.dir") + "/src/test/resources/images/loginButton.png");
		WebDriverManager.chromedriver().version(driverBinaryVersion).setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://opensource-demo.orangehrmlive.com/");
		driver.manage().window().maximize();
		screen.wait(loginButton,5);
		screen.click(loginButton);
		
		screen.type(loginButton,"");

	}

}

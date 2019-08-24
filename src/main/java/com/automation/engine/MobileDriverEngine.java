package com.automation.engine;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class MobileDriverEngine extends Engine {

	public static String appiumHub = "http://0.0.0.0:4723/wd/hub";

	public static void main(String args[]) {

		// Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "Poco F1");
		caps.setCapability("udid", "b6877a6d"); // Give Device ID of your mobile phone
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "9");
		caps.setCapability("appPackage", "com.tictrac.android.oned");
		caps.setCapability("appActivity", "com.tictrac.ui.init.EntryPointActivity");
		caps.setCapability("noReset", "true");

		// Instantiate Appium Driver
		try {
			AppiumDriver<MobileElement> driver = new AppiumDriver<MobileElement>(new URL(appiumHub), caps);
			
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
	}
}
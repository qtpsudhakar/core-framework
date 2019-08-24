package com.automation.engine;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.automation.actions.WebActions;
import com.automation.reporting.ReportLibrary;
import com.automation.testbase.TestBase;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Engine {

	private EventFiringWebDriver webDriver;
	private WebActions webActions;
	private ReportLibrary reportLibrary;
	private String browser, browserVersion;

	public Engine() {
		initialize();
	}

	public void initialize() {
		initalizeWebActions();
		initalizeReportLibrary();
	}

	private void initalizeWebActions() {
		webActions = new WebActions(this);
	}

	private void initalizeReportLibrary() {
		reportLibrary = new ReportLibrary(this);
	}

	public WebActions getWebActions() {
		return webActions;
	}

	public ReportLibrary getReportLibrary() {
		return reportLibrary;
	}

	public EventFiringWebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(EventFiringWebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public void setUpWebDriver(String browserType, Capabilities caps, boolean remoteExecution) throws Exception {
		selectDriverType(browserType, caps, remoteExecution);
	}

	public final void selectDriverType(String browserType, Capabilities caps, boolean remoteExecution)
			throws Exception {
		if (remoteExecution)
			setUpRemoteWebDriver(browserType, caps);
		else
			setUpLocalWebDriver(browserType, caps);

	}

	public void setUpRemoteWebDriver(String browser, Capabilities caps) throws Exception {
		try {
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(createRemoteDriver(caps)));
		} catch (MalformedURLException e) {
			System.out.println("setUpRemoteWebDriver, The url provided was malformed.");
			throw e;
		} catch (SessionNotCreatedException e) {
			System.out.println("setUpRemoteWebDriver, The session could not be created.");
			throw e;
		} catch (WebDriverException e) {
			System.out.println(
					"setUpRemoteWebDriver, The webdriver created on one thread was accessed by another thread or the session could not be created");
			throw e;

		}
	}

	private RemoteWebDriver createRemoteDriver(Capabilities caps) throws Exception {
		RemoteWebDriver remoteWebDriver;
		String gridHub = "";
		switch ("grid") {
		case "grid":
			remoteWebDriver = new RemoteWebDriver(new URL(gridHub), caps);
			break;
		default:
			System.out.println("an attempt to create a remote web driver for grid is failed");
			remoteWebDriver = new RemoteWebDriver(new URL(gridHub), caps);
		}
		return remoteWebDriver;
	}

	public void setUpLocalWebDriver(String browserType, Capabilities caps) throws Exception {
		switch (browserType.toUpperCase()) {
		case "CHROME":
			webDriver = setUpLocalChromeDriver(caps);
			break;
		case "FIREFOX":
			webDriver = setUpLocalFirefoxDriver(caps);
			break;
		case "IE":
			webDriver = setUpLocalIeDriver(caps);
			break;
		default:
			throw new IllegalArgumentException(browserType + " is not supported. Please choose another browser.");
		}
		setBrowser(caps.getBrowserName());
		setBrowserVersion(caps.getVersion());
	}

	public EventFiringWebDriver setUpLocalChromeDriver(Capabilities caps) throws Exception {
		EventFiringWebDriver webDriver;
		ChromeOptions options = (ChromeOptions) caps;
		WebDriverManager.chromedriver().version(TestBase.props.getProperty("chrome.driver.binary.version")).setup();

		if (caps == null)
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new ChromeDriver()));
		else
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new ChromeDriver(options)));

		return webDriver;
	}

	public EventFiringWebDriver setUpLocalFirefoxDriver(Capabilities caps) throws Exception {
		EventFiringWebDriver webDriver;
		FirefoxOptions options = (FirefoxOptions) caps;
		WebDriverManager.firefoxdriver().version(TestBase.props.getProperty("firefox.driver.binary.version")).setup();

		if (caps == null)
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new FirefoxDriver()));
		else
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new FirefoxDriver(options)));

		return webDriver;
	}

	public EventFiringWebDriver setUpLocalIeDriver(Capabilities caps) throws Exception {
		EventFiringWebDriver webDriver;
		InternetExplorerOptions options = (InternetExplorerOptions) caps;
		WebDriverManager.iedriver().version(TestBase.props.getProperty("ie.driver.binary.version")).setup();
		
		if (caps == null)
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new InternetExplorerDriver()));
		else
			webDriver = new EventFiringWebDriver(ThreadGuard.protect(new InternetExplorerDriver(options)));

		return webDriver;
	}
	
	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}


}
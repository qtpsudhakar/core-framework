package com.automation.engine;

import static org.testng.Assert.assertNotNull;

import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.Capabilities;

public class ConcurrentEngine {

	private static ConcurrentHashMap<Long, Engine> engines = new ConcurrentHashMap<Long, Engine>();

	public static Engine getEngine() {
		return engines.get(Thread.currentThread().getId());
	}

	public static void createEngine(Capabilities caps, String browserName, boolean remoteExecution) throws Exception {
		Engine engine = new Engine();
		engine.setUpWebDriver(browserName, caps, remoteExecution);
		assertNotNull(engine.getWebDriver().getWrappedDriver(), "Webdriver was not set up correctly.");
		engines.putIfAbsent(Thread.currentThread().getId(), engine);
	}

	public static void destroyEngine() {
		if (engines.containsKey(Thread.currentThread().getId())) {
			if (!getEngine().getWebDriver().toString().contains("null")) {
				cleanUpDriverProcesses();
			}
			engines.remove(Thread.currentThread().getId());
		}
	}

	public static void closeDriver() {
		getEngine().getWebDriver().close();
	}

	public static void cleanUpDriverProcesses() {
		getEngine().getWebDriver().quit();
	}
}

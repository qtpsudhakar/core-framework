package com.automation.cucumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.ITestContext;

import cucumber.api.event.TestRunFinished;
import cucumber.api.event.TestRunStarted;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.runner.EventBus;
import cucumber.runner.Runner;
import cucumber.runner.ThreadLocalRunnerSupplier;
import cucumber.runner.TimeService;
import cucumber.runner.TimeServiceEventBus;
import cucumber.runtime.BackendModuleBackendSupplier;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.CucumberException;
import cucumber.runtime.FeaturePathFeatureSupplier;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.filter.Filters;
import cucumber.runtime.formatter.PluginFactory;
import cucumber.runtime.formatter.Plugins;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureLoader;
import gherkin.events.PickleEvent;

public class TestNgCucumberRunner {
	
	private final EventBus bus;
	private final Filters filters;
	private final FeaturePathFeatureSupplier featureSupplier;
	private final ThreadLocalRunnerSupplier runnerSupplier;
	private final RuntimeOptions runtimeOptions;
	private ClassLoader classLoader;
	private ResourceLoader resourceLoader;
	
	public TestNgCucumberRunner(ITestContext context, Class<?> clazz) {
		
		Map<String, String> allParameters = context.getCurrentXmlTest().getAllParameters();

		this.classLoader = clazz.getClassLoader();
		resourceLoader = new MultiLoader(classLoader);
		new TestNgParamParser(resourceLoader);
		
		RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
		runtimeOptions = runtimeOptionsFactory.create();

		if (allParameters.containsKey("tags")) {
			runtimeOptions.getTagFilters().add(allParameters.getOrDefault("tags", "@smoke"));
		}

		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
		BackendModuleBackendSupplier backendSupplier = new BackendModuleBackendSupplier(resourceLoader, classFinder,
				runtimeOptions);
		bus = new TimeServiceEventBus(TimeService.SYSTEM);
		new Plugins(classLoader, new PluginFactory(), bus, runtimeOptions);
		FeatureLoader featureLoader = new FeatureLoader(resourceLoader);
		filters = new Filters(runtimeOptions);
		this.runnerSupplier = new ThreadLocalRunnerSupplier(runtimeOptions, bus, backendSupplier);
		featureSupplier = new FeaturePathFeatureSupplier(featureLoader, runtimeOptions);
	}

	public void runScenario(PickleEvent pickle) throws Throwable {
		// Possibly invoked in a multi-threaded context
		Runner runner = runnerSupplier.get();
		TestCaseResultListener testCaseResultListener = new TestCaseResultListener(runner.getBus(),
				runtimeOptions.isStrict());
		runner.runPickle(pickle);
		testCaseResultListener.finishExecutionUnit();

		if (!testCaseResultListener.isPassed()) {
			throw testCaseResultListener.getError();
		}
	}

	public void finish() {
		bus.send(new TestRunFinished(bus.getTime(), bus.getTimeMillis()));
	}

	/**
	 * @return returns the cucumber scenarios as a two dimensional array of
	 *         {@link PickleEventWrapper} scenarios combined with their
	 *         {@link CucumberFeatureWrapper} feature.
	 */
	public Object[][] provideScenarios() {
		try {
			List<Object[]> scenarios = new ArrayList<Object[]>();
			List<CucumberFeature> features = getFeatures();
			for (CucumberFeature feature : features) {
				for (PickleEvent pickle : feature.getPickles()) {
					if (filters.matchesFilters(pickle)) {
						scenarios.add(new Object[] { new PickleEventWrapperImpl(pickle),
								new CucumberFeatureWrapperImpl(feature) });
					}
				}
			}
			return scenarios.toArray(new Object[][] {});
		} catch (CucumberException e) {
			return new Object[][] { new Object[] { new CucumberExceptionWrapper(e), null } };
		}
	}

	/**
	 * @return returns the cucumber features as a two dimensional array of
	 *         {@link CucumberFeatureWrapper} objects.
	 */
	public Object[][] provideFeatures() {
		List<CucumberFeature> features = getFeatures();
		List<Object[]> featuresList = new ArrayList<Object[]>(features.size());
		for (CucumberFeature feature : features) {
			featuresList.add(new Object[] { new CucumberFeatureWrapperImpl(feature) });
		}
		return featuresList.toArray(new Object[][] {});
	}

	List<CucumberFeature> getFeatures() {

		List<CucumberFeature> features = featureSupplier.get();
		bus.send(new TestRunStarted(bus.getTime(), bus.getTimeMillis()));
		for (CucumberFeature feature : features) {
			feature.sendTestSourceRead(bus);
		}
		return features;
	}

}

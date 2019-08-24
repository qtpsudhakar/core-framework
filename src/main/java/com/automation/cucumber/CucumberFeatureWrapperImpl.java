package com.automation.cucumber;

import cucumber.runtime.model.CucumberFeature;

public class CucumberFeatureWrapperImpl implements CucumberFeatureWrapper {

	private final CucumberFeature cucumberFeature;

	CucumberFeatureWrapperImpl(CucumberFeature cucumberFeature) {
		this.cucumberFeature = cucumberFeature;
	}

	@Override
	public String toString() {
		return "\"" + cucumberFeature.getGherkinFeature().getFeature().getName() + "\"";
	}

	public CucumberFeature getCucumberFeature() {
		return cucumberFeature;
	}

}
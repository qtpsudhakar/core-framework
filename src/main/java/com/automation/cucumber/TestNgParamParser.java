package com.automation.cucumber;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cucumber.runtime.io.ResourceLoader;
import io.cucumber.core.model.FeaturePath;
import io.cucumber.core.model.FeatureWithLines;
import io.cucumber.core.model.RerunLoader;

public class TestNgParamParser {

	Map<URI, Set<Integer>> parsedLineFilters = new HashMap<>();
	List<URI> parsedFeaturePaths = new ArrayList<>();
	private final RerunLoader rerunLoader;

	public TestNgParamParser(ResourceLoader resourceLoader) {
		this.rerunLoader = new RerunLoader(resourceLoader);
	}

	public List<URI> parseFeaturePath1(List<String> args) {
		while (!args.isEmpty()) {
			String arg = args.remove(0).trim();
			{
				if (arg.startsWith("@")) {
					URI rerunFile = FeaturePath.parse(arg.substring(1));
					processPathWitheLinesFromRerunFile(parsedLineFilters, parsedFeaturePaths, rerunFile);
				} else if (!arg.isEmpty()) {
					FeatureWithLines featureWithLines = FeatureWithLines.parse(arg);
					processFeatureWithLines(parsedLineFilters, parsedFeaturePaths, featureWithLines);
				}
			}
		}
		return parsedFeaturePaths;
	}

	public List<URI> parseFeaturePath(List<String> args) {
		for(int i=0; i<args.size();i++) {
			//while (!args.isEmpty()) {
				//String arg = args.remove(0).trim();
				String arg = args.get(0);
				{
					if (arg.startsWith("@")) {
						URI rerunFile = FeaturePath.parse(arg.substring(1));
						processPathWitheLinesFromRerunFile(parsedLineFilters, parsedFeaturePaths, rerunFile);
					} else if (!arg.isEmpty()) {
						FeatureWithLines featureWithLines = FeatureWithLines.parse(arg);
						processFeatureWithLines(parsedLineFilters, parsedFeaturePaths, featureWithLines);
					}
				}
			//}
			
		}
		return parsedFeaturePaths;
	}

	private void processFeatureWithLines(Map<URI, Set<Integer>> parsedLineFilters, List<URI> parsedFeaturePaths,
			FeatureWithLines featureWithLines) {
		parsedFeaturePaths.add(featureWithLines.uri());
		addLineFilters(parsedLineFilters, featureWithLines.uri(), featureWithLines.lines());
	}

	private void processPathWitheLinesFromRerunFile(Map<URI, Set<Integer>> parsedLineFilters,
			List<URI> parsedFeaturePaths, URI rerunPath) {

		for (FeatureWithLines featureWithLines : rerunLoader.load(rerunPath)) {
			processFeatureWithLines(parsedLineFilters, parsedFeaturePaths, featureWithLines);
		}
	}

	private void addLineFilters(Map<URI, Set<Integer>> parsedLineFilters, URI key, Set<Integer> lines) {
		if (lines.isEmpty()) {
			return;
		}
		if (parsedLineFilters.containsKey(key)) {
			parsedLineFilters.get(key).addAll(lines);
		} else {
			parsedLineFilters.put(key, new TreeSet<>(lines));
		}
	}

	public Map<URI, Set<Integer>> getParsedLineFilters() {
		return parsedLineFilters;
	}

	public void setParsedLineFilters(Map<URI, Set<Integer>> parsedLineFilters) {
		this.parsedLineFilters = parsedLineFilters;
	}

	public List<URI> getParsedFeaturePaths() {
		return parsedFeaturePaths;
	}

	public void setParsedFeaturePaths(List<URI> parsedFeaturePaths) {
		this.parsedFeaturePaths = parsedFeaturePaths;
	}

}

package com.prometheus.brainbash.selenium;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("com/prometheus/brainbash/selenium")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "com.prometheus.brainbash.selenium")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, summary")
public class SeleniumRunner {

}

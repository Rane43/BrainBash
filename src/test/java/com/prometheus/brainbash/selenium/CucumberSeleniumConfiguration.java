package com.prometheus.brainbash.selenium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prometheus.brainbash.test_helper.DatabaseManager;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CucumberSeleniumConfiguration {
	
	@Autowired
	protected DatabaseManager databaseManager;
}

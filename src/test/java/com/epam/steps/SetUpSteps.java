package com.epam.steps;

import com.epam.providers.dataProviders.ConfigPropertiesProviders;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetUpSteps {
    private static final ConfigPropertiesProviders propertyProvider = new ConfigPropertiesProviders();
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Given("Setup Rest Assured")
    public void setupRestAssured() {
        logger.info("Base URI is '{}'", propertyProvider.getBASE_URI());
        RestAssured.baseURI = propertyProvider.getBASE_URI();
    }
}

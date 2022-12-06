package com.epam.steps;

import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.ConfigPropertiesProviders;
import com.epam.providers.dataProviders.UserDataProvider;
import com.epam.utils.RequestUtils;
import com.epam.utils.ResponseUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonSteps {
    private static final ConfigPropertiesProviders propertyProvider = new ConfigPropertiesProviders();
    private final Logger logger = LoggerFactory.getLogger(CommonSteps.class);
    @Given("Setup Rest Assured")
    public void setupRestAssured() {
        logger.info("Base URI is '{}'", propertyProvider.getBASE_URI());
        RestAssured.baseURI = propertyProvider.getBASE_URI();
    }

    @When("Request to GET by endpoint {}")
    public void getAllQuestionnaires(String endpoint) {
        String questionnaires = RequestUtils.get(endpoint).extract().asPrettyString();
        logger.info("Request to get by endpoint {}", questionnaires);
    }

    @Then("Validate status code is {}")
    public void statusCodeShouldBe(int expectedStatusCode) {
        int actualStatusCode = ResponseUtils.getResponse().extract().statusCode();
        logger.info("Validate status code. Expected {}, actual {}", expectedStatusCode, actualStatusCode);
        Assertions.assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
    }
}

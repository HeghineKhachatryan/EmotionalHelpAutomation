package com.epam.steps;

import com.epam.helpers.PropertiesWriter;
import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.ConfigPropertiesProviders;
import com.epam.providers.dataProviders.Endpoints;
import com.epam.providers.dataProviders.SharedTestData;
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

    @When("Request to POST {} {} {} {} by endpoint {}")
    public void requestToPOSTByEndpoint(String param, String param1, String param2, String bodyName, String endpoint) {
        String body = getBody(bodyName, param, param1, param2);
        if (endpoint.equals(Endpoints.RESET_PASSWORD.name())) {
            RequestUtils.post(Endpoints.RESET_PASSWORD.getEndpoint(), body, SharedTestData.getJWTToken());
        } else {
            RequestUtils.post(Endpoints.valueOf(endpoint).getEndpoint(), body).extract().asPrettyString();
        }

        if (SharedTestData.getPassword() != null) {
            PropertiesWriter.writeInPropertyFile("src/main/resources/userPassword.properties",
                    "existedUserPassword", SharedTestData.getPassword());
        }
        logger.info("Request to POST by endpoint {}", endpoint);
    }

    private String getBody(String bodyName, String param, String param1, String param2) {
        logger.info("Get body for {}", bodyName);
        switch (bodyName) {
            case "newUser":
                String bodyForNewUser = BodyProvider.createBodyForNewUser();
                logger.info("Body for new user is -> {}", bodyForNewUser);
                return bodyForNewUser;
            case "questionnaires":
                String bodyForQuestionnaires = BodyProvider.createBodyForQuestionnaires();
                logger.info("Body for questionnaires is -> {}", bodyForQuestionnaires);
                return bodyForQuestionnaires;
            case "invalidUser":
                String bodyForInvalidUser = BodyProvider.createInvalidUserBody(param, param1, param2);
                logger.info("Body for invalid user is -> {}", bodyForInvalidUser);
                return bodyForInvalidUser;
            case "resetPassword":
                String newPassword = UserDataProvider.generateStrongPassword();
                SharedTestData.setPassword(newPassword);
                String bodyForResettingPassword = BodyProvider.createBodyForResettingPassword(newPassword);
                logger.info("Body for resetting password is -> {}", bodyForResettingPassword);
                return bodyForResettingPassword;
            case "resetForgottenPassword":
                String passwordForResetting = UserDataProvider.generateStrongPassword();
                SharedTestData.setPassword(passwordForResetting);
                String bodyForForgottenReset = BodyProvider.createBodyForResettingForgottenPassword(passwordForResetting);
                logger.info("Body for resetting forgotten password is -> {}", bodyForForgottenReset);
                return bodyForForgottenReset;
            default:
                throw new IllegalArgumentException("There is no such type of body, check it, please");
        }
    }
}

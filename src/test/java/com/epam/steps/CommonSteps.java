package com.epam.steps;

import com.epam.helpers.PropertiesWriter;
import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.ConfigPropertiesProviders;
import com.epam.providers.dataProviders.Endpoints;
import com.epam.providers.dataProviders.SharedTestData;
import com.epam.providers.dataProviders.UserDataProvider;
import com.epam.utils.RequestUtils;
import com.epam.utils.ResponseUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

import java.util.Map;

@Slf4j
public class CommonSteps {
    private static final ConfigPropertiesProviders propertyProvider = new ConfigPropertiesProviders();

    @Given("Setup Rest Assured")
    public void setupRestAssured() {
        log.info("Base URI is '{}'", propertyProvider.getBASE_URI());
        RestAssured.baseURI = propertyProvider.getBASE_URI();
    }

    @And("Validate response body against JSON schema for {}")
    public void validateResponseBodyAgainstJSONSchema(String schemaName) {
        log.info("Validate response body against JSON schema");
        ResponseUtils.validateResponseAgainstJSONSchema("schemas/" + schemaName + ".json");
    }

    @When("Request to GET by endpoint {}")
    public void getAllQuestionnaires(String endpoint) {
        if (endpoint.contains(Endpoints.CONFIRM_EMAIL.name())) {
            RequestUtils.get(Endpoints.CONFIRM_EMAIL.url + SharedTestData.getMessageText());
        } else {
            RequestUtils.get(endpoint);
        }
        log.info("Request to get by endpoint {}. Response body is {}",
                endpoint, ResponseUtils.getResponse().extract().body().asPrettyString());
    }

    @Then("Validate status code is {}")
    public void statusCodeShouldBe(int expectedStatusCode) {
        int actualStatusCode = ResponseUtils.getResponse().extract().statusCode();
        log.info("Validate status code. Expected {}, actual {}", expectedStatusCode, actualStatusCode);
        Assertions.assertThat(actualStatusCode).isEqualTo(expectedStatusCode);
    }

    @When("Request to POST by endpoint")
    public void requestToPOSTByEndpoint(DataTable table) {
        Map<String, String> arguments = table.transpose().asMaps().get(0);
        String email = arguments.getOrDefault("email", UserDataProvider.generateEmail());
        String password = arguments.getOrDefault("password", UserDataProvider.generateStrongPassword());
        String name = arguments.getOrDefault("name", UserDataProvider.generateName());
        String bodyName = arguments.get("bodyName");
        String endpoint = arguments.get("endpoint");
        String body = getBody(bodyName, name, email, password);
        if (endpoint.equals(Endpoints.RESET_PASSWORD.name())) {
            RequestUtils.post(Endpoints.RESET_PASSWORD.url, body, SharedTestData.getJWTToken());
        } else {
            RequestUtils.post(Endpoints.valueOf(endpoint).url, body).extract().asPrettyString();
        }
        log.info("Request to POST by endpoint {}", endpoint);
    }

    @And("Save password value")
    public void savePasswordValue() {
        PropertiesWriter.writeInPropertyFile("src/main/resources/userPassword.properties",
                "existedUserPassword", SharedTestData.getPassword());
    }

    private String getBody(String bodyName, String name, String email, String password) {
        log.info("Get body for {}", bodyName);
        switch (bodyName) {
            case "userWithCorrectCredentials":
                String bodyForUserWithCorrectCredentials = BodyProvider.createBodyForUserWithCorrectCredentials();
                log.info("Body for new user is -> {}", bodyForUserWithCorrectCredentials);
                return bodyForUserWithCorrectCredentials;
            case "newUser":
                String bodyForNewUser = BodyProvider.createBodyForNewUser();
                log.info("Body for new user is -> {}", bodyForNewUser);
                return bodyForNewUser;
            case "questionnaires":
                String bodyForQuestionnaires = BodyProvider.createBodyForQuestionnaires();
                log.info("Body for questionnaires is -> {}", bodyForQuestionnaires);
                return bodyForQuestionnaires;
            case "invalidUser":
                String bodyForInvalidUser = BodyProvider.createInvalidUserBody(name, email, password);
                log.info("Body for invalid user is -> {}", bodyForInvalidUser);
                return bodyForInvalidUser;
            case "resetPassword":
                String newPassword = UserDataProvider.generateStrongPassword();
                SharedTestData.setPassword(newPassword);
                String bodyForResettingPassword = BodyProvider.createBodyForResettingPassword(newPassword);
                log.info("Body for resetting password is -> {}", bodyForResettingPassword);
                return bodyForResettingPassword;
            case "resetForgottenPassword":
                String passwordForResetting = UserDataProvider.generateStrongPassword();
                SharedTestData.setPassword(passwordForResetting);
                String bodyForForgottenReset = BodyProvider.createBodyForResettingForgottenPassword(passwordForResetting);
                log.info("Body for resetting forgotten password is -> {}", bodyForForgottenReset);
                return bodyForForgottenReset;
            default:
                throw new IllegalArgumentException("There is no such type of body, check it, please");
        }
    }
}

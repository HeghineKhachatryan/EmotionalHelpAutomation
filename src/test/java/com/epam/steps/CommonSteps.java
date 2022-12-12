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
import org.assertj.core.api.SoftAssertions;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonSteps {
    private static final ConfigPropertiesProviders propertyProvider = new ConfigPropertiesProviders();
    private final Map<String, Object> bodyParameters = new HashMap<>();

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

    @Then("Request to GET by token, id and endpoint {}")
    public void requestToGETByTokenAndEndpoint(String endpoint) {
        RequestUtils.get(endpoint.replaceAll("\\{id}", SharedTestData.getUserID() + ""), SharedTestData.getJWTToken());
        log.info("Request to get by endpoint {}. Response body is {}",
                endpoint, ResponseUtils.getResponse().extract().body().asPrettyString());
    }


    @And("Save ID of the user")
    public void saveIDOfTheUser() {
        SharedTestData.setUserID(ResponseUtils.getIDFromResponse("userResponseDTO.id"));
    }

    @When("Request to PATCH by endpoint {}")
    public void requestToPATCHByEndpoint(String endpoint) {
        bodyParameters.put("name", "my new name is " + UserDataProvider.generateName());
        bodyParameters.put("gender", "Female");
        bodyParameters.put("country", "my country is Armenia");
        bodyParameters.put("role", "my role is Admin");
        String url = Endpoints.valueOf(endpoint).url.replaceAll("\\{id}", SharedTestData.getUserID() + "");
        RequestUtils.patch(url, BodyProvider.getBody("updateUser", bodyParameters),
                SharedTestData.getJWTToken());
        log.info("Request to patch with endpoint {}, Bearer {} token and body", url, SharedTestData.getJWTToken());
    }

    @And("Validate required field value has been changed")
    public void validateRequiredFieldValueHasBeenChanged() {
        log.info("Validate required field value has been changed -> name, gender, country and role");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(ResponseUtils.getStringFromResponse("name")).isEqualTo(bodyParameters.get("name"));
        softAssertions.assertThat(ResponseUtils.getStringFromResponse("gender")).isEqualTo(bodyParameters.get("gender"));
        softAssertions.assertThat(ResponseUtils.getStringFromResponse("country")).isEqualTo(bodyParameters.get("country"));
        softAssertions.assertThat(ResponseUtils.getStringFromResponse("role")).isEqualTo(bodyParameters.get("role"));
        softAssertions.assertAll();
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
            case "specialists":
                String specialists = BodyProvider.createBodyForSpecialists();
                log.info("Body for specialists -> {}", specialists);
                return specialists;
            case "projects":
            case "articles":
                String articles = BodyProvider.createBodyForArticles();
                log.info("Body for {} -> {}", bodyName, articles);
                return articles;
            default:
                throw new IllegalArgumentException("There is no such type of body, check it, please");
        }
    }
}

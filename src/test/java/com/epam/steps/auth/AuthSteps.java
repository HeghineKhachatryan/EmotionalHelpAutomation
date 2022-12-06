package com.epam.steps.auth;

import com.epam.helpers.PropertiesWriter;
import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.ConfigPropertiesProviders;
import com.epam.providers.dataProviders.MessagesProviders;
import com.epam.providers.dataProviders.SharedTestData;
import com.epam.providers.dataProviders.UserDataProvider;
import com.epam.steps.CommonSteps;
import com.epam.utils.RequestUtils;
import com.epam.utils.ResponseUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AuthSteps {

    private static final ConfigPropertiesProviders propertyProvider = new ConfigPropertiesProviders();
    private final Logger logger = LoggerFactory.getLogger(CommonSteps.class);
    private final Map<String, Object> bodyParameters = new HashMap<>();

    @When("Request to POST a new user")
    public void requestToPOSTByEndpoint() {
        bodyParameters.put("name", UserDataProvider.generateName());
        bodyParameters.put("email", UserDataProvider.generateEmail());
        bodyParameters.put("password", UserDataProvider.generateStrongPassword());
        String body = BodyProvider.getBody("signup", bodyParameters);
        RequestUtils.post(propertyProvider.getSignUpEndpoint(), body);
        logger.info("Make a post request with {} endpoint and {} body", propertyProvider.getSignUpEndpoint(), body);
    }

    @When("Request to POST a new user with {}, {} and {}")
    public void requestToPOSTANewUserByEndpoint(String name, String email, String password) {
        bodyParameters.put("name", name);
        bodyParameters.put("email", email);
        bodyParameters.put("password", password);
        SharedTestData.setCurrentPassword(password);
        SharedTestData.setCurrentEmail(email);
        String body = BodyProvider.getBody("signup", bodyParameters);
        RequestUtils.post(propertyProvider.getSignUpEndpoint(), body);
        logger.info("Make a post request with {} endpoint and {} body", propertyProvider.getSignUpEndpoint(), body);
    }

    @And("Validate error message contains {}")
    public void validateErrorMessageContains(String text) {
        String message = ResponseUtils.getStringFromResponse("message");
        logger.info("Validate error message -> {} contains text ->{}", message, text);
        Assertions.assertThat(message.contains(text))
                .withFailMessage("message doesn't contains the provided text, but it should")
                .isTrue();
    }

    @When("Login with existed email and password")
    public void loginWithExistedEmailAndPassword() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", UserDataProvider.getExistedEmail());
        jsonObject.put("password", UserDataProvider.getExistedPassword());
        SharedTestData.setCurrentPassword(UserDataProvider.getExistedPassword());
        RequestUtils.get(propertyProvider.getLoginEndpoint(), jsonObject.toJSONString());
        logger.info("Login with existed email and password -> body - {}", jsonObject.toJSONString());
    }

    @And("Save access token value and type")
    public void saveAccessTokenValueAndType() {
        String token = ResponseUtils.getStringFromResponse("jwtAccess");
        String type = ResponseUtils.getStringFromResponse("type");
        logger.info("Save access token type ({}) and value ({}) from the response", type, token);
        SharedTestData.setJWTToken(token);
        SharedTestData.setTokenType(type);
    }

    @When("Login with incorrect credentials - {} and {}")
    public void loginWithIncorrectCredentials(String username, String password) {
        logger.info("Login with incorrect credentials - username is '{}' and password is '{}'", username, password);
        bodyParameters.put("username", username);
        bodyParameters.put("password", password);
        RequestUtils.get(propertyProvider.getLoginEndpoint(), BodyProvider.getBody("login", bodyParameters));
    }

    @And("Request to reset password by provided token")
    public void requestToResetPasswordByProvidedToken() {
        logger.info("Request to reset password by provided token and body");
        String newPassword = UserDataProvider.generateStrongPassword();
        bodyParameters.put("currentPassword", UserDataProvider.getExistedPassword());
        bodyParameters.put("newPassword", newPassword);
        bodyParameters.put("conformNewPassword", newPassword);
        PropertiesWriter.writeInPropertyFile("src/main/resources/userPassword.properties",
                "existedUserPassword", newPassword);
        RequestUtils.post(propertyProvider.getResetPasswordEndpoint(),
                BodyProvider.getBody("resetPassword", bodyParameters),
                SharedTestData.getTokenType() + " " + SharedTestData.getJWTToken());
    }

    @And("Validate success message for resetting password")
    public void validateSuccessMessageForResettingPassword() {
        ResponseUtils.getResponse().extract().asPrettyString();
        logger.info("Message from response is {}. Message to validate is {}", ResponseUtils.getStringFromResponse("message"),
                MessagesProviders.getResetPasswordSuccessMsg());
        Assertions.assertThat(ResponseUtils.getStringFromResponse("message"))
                .isEqualTo(MessagesProviders.getResetPasswordSuccessMsg());
    }
}

package com.epam.steps.auth;

import com.epam.helpers.PropertiesWriter;
import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.*;
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
    private final Logger logger = LoggerFactory.getLogger(CommonSteps.class);
    private Map<String, Object> bodyParameters = new HashMap<>();

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
        RequestUtils.post(Endpoints.LOGIN.getEndpoint(), jsonObject.toJSONString());
        logger.info("Login with existed email and password -> body - {}", jsonObject.toJSONString());
    }

    @And("Save access token value and type")
    public void saveAccessTokenValueAndType() {
        String token = ResponseUtils.getStringFromResponse("jwtAccess");
        logger.info("Save access token ({}) from the response", token);
        SharedTestData.setJWTToken(token);
    }

    @When("Login with incorrect credentials - {} and {}")
    public void loginWithIncorrectCredentials(String username, String password) {
        logger.info("Login with incorrect credentials - username is '{}' and password is '{}'", username, password);
        bodyParameters.put("username", username);
        bodyParameters.put("password", password);
        RequestUtils.post(Endpoints.LOGIN.getEndpoint(), BodyProvider.getBody("login", bodyParameters));
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

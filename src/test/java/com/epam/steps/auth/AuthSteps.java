package com.epam.steps.auth;

import com.epam.helpers.PropertiesWriter;
import com.epam.mail.MailMessageProvider;
import com.epam.providers.bodyProviders.BodyProvider;
import com.epam.providers.dataProviders.Endpoints;
import com.epam.providers.dataProviders.MessagesProviders;
import com.epam.providers.dataProviders.SharedTestData;
import com.epam.providers.dataProviders.UserDataProvider;
import com.epam.utils.RequestUtils;
import com.epam.utils.ResponseUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthSteps {
    private Map<String, Object> bodyParameters = new HashMap<>();

    @And("Validate error message contains {}")
    public void validateErrorMessageContains(String text) {
        String message = ResponseUtils.getStringFromResponse("message");
        log.info("Validate error message -> {} contains text ->{}", message, text);
        Assertions.assertThat(message.contains(text))
                .withFailMessage("message doesn't contains the provided text, but it should")
                .isTrue();
    }

    @When("Login with existed email and password")
    public void loginWithExistedEmailAndPassword() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", UserDataProvider.getExistedEmail());
        jsonObject.put("password", UserDataProvider.getExistedPassword());
        RequestUtils.post(Endpoints.LOGIN.url, jsonObject.toJSONString());
        log.info("Login with existed email and password -> body - {}", jsonObject.toJSONString());
    }

    @And("Save access token value and type")
    public void saveAccessTokenValueAndType() {
        String token = ResponseUtils.getStringFromResponse("jwtAccess");
        log.info("Save access token ({}) from the response", token);
        SharedTestData.setJWTToken(token);
    }

    @When("Login with incorrect credentials - {} and {}")
    public void loginWithIncorrectCredentials(String username, String password) {
        log.info("Login with incorrect credentials - username is '{}' and password is '{}'", username, password);
        bodyParameters.put("username", username);
        bodyParameters.put("password", password);
        RequestUtils.post(Endpoints.LOGIN.url, BodyProvider.getBody("login", bodyParameters));
    }

    @And("Validate success message for resetting password")
    public void validateSuccessMessageForResettingPassword() {
        ResponseUtils.getResponse().extract().asPrettyString();
        log.info("Message from response is {}. Message to validate is {}", ResponseUtils.getStringFromResponse("message"),
                MessagesProviders.getResetPasswordSuccessMsg());
        Assertions.assertThat(ResponseUtils.getStringFromResponse("message"))
                .isEqualTo(MessagesProviders.getResetPasswordSuccessMsg());
    }

    @When("Request to POST by query params: {string} {string} using endpoint {string}")
    public void requestToPOSTByQueryParamsUsingEndpoint(String queryKey, String queryValue, String endpoint) {
        log.info("Request to POST by query params:{} {} using endpoint {}", queryKey, queryValue, endpoint);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(queryKey, queryValue);
        RequestUtils.post(Endpoints.valueOf(endpoint).url, queryParams);
        System.out.println(ResponseUtils.getResponse().extract().asPrettyString());
    }

    @And("Get {} from email message")
    public void getCodeFromEmailMessage(String text) throws InterruptedException {
        log.info("Get code from email message. In case it's not sent yet, wait for 3 minutes.");
        Thread.sleep(10000);
        if (text.equals("code")) {
            SharedTestData.setMessageText(MailMessageProvider.getCodeFromMessage());
        } else if (text.equals("link")) {
            SharedTestData.setMessageText(MailMessageProvider.getLinkFromMessage());
        } else {
            throw new IllegalArgumentException("There is no such '" + text + "' in the email message");
        }
    }
}

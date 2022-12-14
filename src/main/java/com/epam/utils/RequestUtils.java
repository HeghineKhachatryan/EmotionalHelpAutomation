package com.epam.utils;

import com.epam.providers.dataProviders.SharedTestData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class RequestUtils {
    private final static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    private static ValidatableResponse response;

    private RequestUtils() {

    }

    public static ValidatableResponse getResponse() {
        logger.info("Get response");
        return response;
    }

    public static ValidatableResponse get(String endpoint) {
        logger.info("Get response by requesting all {}", endpoint);
        return response = RestAssured
                .given()
                .when()
                .get(endpoint)
                .then();
    }

    public static ValidatableResponse get(String endpoint, String token) {
        logger.info("Get response by requesting {}", endpoint);
        return response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + token)
                .when()
                .get(endpoint)
                .then();
    }

    public static ValidatableResponse get(String endpoint, Object body) {
        logger.info("Get response by requesting all {}", endpoint);
        return response = RestAssured
                .given()
                .spec(getRequestSpecification(body))
                .when()
                .get(endpoint)
                .then();
    }

    public static ValidatableResponse post(String endpoint, Object body) {
        logger.info("Create new {} with the following body -> {}", endpoint, body);
        return response = RestAssured
                .given()
                .spec(getRequestSpecification(body))
                .when()
                .post(endpoint)
                .then();
    }

    public static ValidatableResponse patch(String endpoint, Object body, String token) {
        logger.info("Update {} with body {} and provided token {}", endpoint, body, token);
        return response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + token)
                .spec(getRequestSpecification(body))
                .when()
                .patch(endpoint)
                .then();
    }

    public static ValidatableResponse post(String endpoint, Map<String, String> queryParams) {
        logger.info("Create new {} with query params", endpoint);
        return response = RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .queryParams(queryParams)
                .when()
                .post(endpoint)
                .then();
    }

    public static ValidatableResponse post(String endpoint, Object body, String token) {
        logger.info("Create new {} with the following body -> {} and token {}", endpoint, body, token);
        return response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + token)
                .spec(getRequestSpecification(body))
                .when()
                .post(endpoint)
                .then();
    }

    private static RequestSpecification getRequestSpecification(Object body) {
        logger.info("Accept and set content type {}, set body {}", ContentType.JSON, body);
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        return requestSpecBuilder
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBody(body)
                .build();

    }
}

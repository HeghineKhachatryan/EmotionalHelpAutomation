package com.epam.utils;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResponseUtils {
    private final static Logger logger = LoggerFactory.getLogger(ResponseUtils.class);

    public static ValidatableResponse getResponse() {
        return RequestUtils.getResponse();
    }

    public static <T> T getObjectFromResponse(String path, Class<T> classType) {
        logger.info("Get object of {} type from response", classType.getName());
        return getResponse()
                .extract()
                .jsonPath()
                .getObject(path, classType);
    }

    public static int getIDFromResponse(String path) {
        logger.info("Get ID from response");
        return getResponse()
                .extract()
                .jsonPath()
                .getInt(path);
    }

    public static String getStringFromResponse(String path) {
        logger.info("Get String from response");
        return getResponse()
                .extract()
                .jsonPath()
                .getString(path);
    }

    public static void validateResponseAgainstJsonSchema(String filePath) {
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory
                .newBuilder()
                .setValidationConfiguration(
                        ValidationConfiguration
                                .newBuilder()
                                .setDefaultVersion(SchemaVersion.DRAFTV4)
                                .freeze()
                )
                .freeze();

        getResponse()
                .assertThat()
                .body(
                        JsonSchemaValidator
                                .matchesJsonSchemaInClasspath(filePath)
                                .using(jsonSchemaFactory)
                );
    }
}

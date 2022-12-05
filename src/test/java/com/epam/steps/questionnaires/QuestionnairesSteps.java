package com.epam.steps.questionnaires;

import com.epam.utils.RequestUtils;
import com.epam.utils.ResponseUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionnairesSteps {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @And("Validate response body against JSON schema for {}")
    public void validateResponseBodyAgainstJSONSchema(String schemaName) {
        logger.info("Validate response body against JSON schema");
        ResponseUtils.validateResponseAgainstJSONSchema("schemas/" + schemaName + ".json");
    }
}

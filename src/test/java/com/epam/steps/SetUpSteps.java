package com.epam.steps;

import io.restassured.RestAssured;

public class SetUpSteps {

    public static void getBaseURI() {
        RestAssured.baseURI = "";
    }
}

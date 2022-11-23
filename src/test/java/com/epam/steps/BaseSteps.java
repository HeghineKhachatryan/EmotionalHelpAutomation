package com.epam.steps;

import com.epam.driver.WebDriverProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseSteps {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void setup() {
        WebDriverProvider.getDriver();
    }

    public static void quitDriver() {
        WebDriverProvider.quitDriver();
    }
}

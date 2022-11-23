package com.epam.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverProvider {

    private volatile static WebDriver chromeDriver;

    private WebDriverProvider() {
    }

    public static WebDriver getDriver() {
            if (chromeDriver == null) {
                synchronized (WebDriverProvider.class) {
                    if (chromeDriver == null) {
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--start-maximized");
                        options.addArguments("--incognito");
                        chromeDriver = new ChromeDriver(options);
                    }
                }
            }
            return chromeDriver;
    }

    public static void quitDriver() {
            chromeDriver.quit();
            chromeDriver = null;
    }
}

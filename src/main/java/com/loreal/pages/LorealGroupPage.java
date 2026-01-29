package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class LorealGroupPage {

    private static final Logger log = Logger.getLogger(LorealGroupPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Generic page header locators (robust)
    private final By mainHeaderH1 = By.xpath("//main//h1 | //section//h1 | //div[contains(@class,'hero') or contains(@class,'header')]//h1");
    private final By anyH1 = By.tagName("h1");

    public LorealGroupPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for the 'Group' page to load and display header.");
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/en/group/"),
                    ExpectedConditions.visibilityOfElementLocated(mainHeaderH1),
                    ExpectedConditions.visibilityOfElementLocated(anyH1)
            ));
        } catch (TimeoutException e) {
            throw new TimeoutException("Group page did not load properly.", e);
        }
    }

    public String getHeaderText() {
        try {
            WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(mainHeaderH1));
            return h1.getText().trim();
        } catch (TimeoutException e) {
            try {
                WebElement h1 = wait.until(ExpectedConditions.visibilityOfElementLocated(anyH1));
                return h1.getText().trim();
            } catch (TimeoutException ex) {
                return "";
            }
        }
    }

    public boolean isOnGroupUrl() {
        return driver.getCurrentUrl().toLowerCase().contains("/en/group/");
    }
}

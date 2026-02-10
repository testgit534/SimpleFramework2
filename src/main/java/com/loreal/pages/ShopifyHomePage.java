package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class ShopifyHomePage {

    private static final Logger log = Logger.getLogger(ShopifyHomePage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By headerRoot = By.tagName("header");

    // From Browser-use JSON
    private final By signUpLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[4]");
    private final By signUpLinkId = By.id("customer_register_link");

    private final By loginLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[3]");
    private final By loginLinkId = By.id("customer_login_link");

    // Search submit (seen clicked in JSON step 10)
    private final By searchSubmitXpath = By.xpath("html/body/header/div[1]/div[1]/div/form/input[2]");
    private final By searchSubmitId = By.id("search-submit");

    public ShopifyHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForHeader() {
        log.info("Waiting for Shopify site header to be visible.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerRoot));
    }

    public void clickSignUp() {
        log.info("Clicking 'Sign up' link from header.");
        WebElement link = findVisible(signUpLinkXpath, signUpLinkId);
        link.click();
    }

    public void clickLogin() {
        log.info("Clicking 'Log In' link from header.");
        WebElement link = findVisible(loginLinkXpath, loginLinkId);
        link.click();
    }

    public void clickSearchSubmitIfVisible() {
        try {
            log.info("Attempting to click search submit in header (if present).");
            WebElement btn = waitShortForAny(searchSubmitXpath, searchSubmitId);
            if (btn != null && btn.isDisplayed()) {
                btn.click();
            }
        } catch (Exception ignored) {
        }
    }

    private WebElement findVisible(By... candidates) {
        for (By by : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate element using provided locators.");
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        return null;
    }
}

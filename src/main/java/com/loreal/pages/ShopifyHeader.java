package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyHeader {

    private static final Logger log = Logger.getLogger(ShopifyHeader.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By headerTag = By.tagName("header");

    // Locators from Browser-use JSON with reliable fallbacks
    private final By signUpLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[4]");
    private final By signUpLinkId = By.id("customer_register_link");
    private final By loginLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[3]");
    private final By loginLinkId = By.id("customer_login_link");

    public ShopifyHeader(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForHeader() {
        log.info("Waiting for Shopify site header to be visible.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(headerTag));
    }

    public void clickSignUp() {
        log.info("Clicking Sign up link from header.");
        WebElement link = findClickable(signUpLinkXpath, signUpLinkId);
        link.click();
    }

    public void clickLogin() {
        log.info("Clicking Log In link from header.");
        WebElement link = findClickable(loginLinkXpath, loginLinkId);
        try {
            link.click();
        } catch (ElementClickInterceptedException e) {
            log.info("Click intercepted (possibly by captcha). Navigating directly to login URL as fallback.");
            driver.navigate().to("https://sauce-demo.myshopify.com/account/login");
        }
    }

    private WebElement findClickable(By... candidates) {
        for (By by : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(7))
                        .until(ExpectedConditions.elementToBeClickable(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to find clickable element using fallback locators.");
    }
}

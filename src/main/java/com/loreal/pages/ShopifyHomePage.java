package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyHomePage {

    private static final Logger log = Logger.getLogger(ShopifyHomePage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By header = By.tagName("header");

    // Sign up link from Browser-use JSON + fallbacks
    private final By signUpLinkId = By.id("customer_register_link");
    private final By signUpLinkXpath = By.xpath("html/body/header/div[1]/div[2]/nav/a[4]");
    private final By signUpLinkText = By.xpath("//a[normalize-space()='Sign up' or normalize-space()='Sign Up' or contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sign up')]");

    public ShopifyHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForHeader() {
        log.info("Waiting for Shopify demo home header to be visible.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
    }

    public void clickSignUp() {
        log.info("Clicking 'Sign up' link.");
        WebElement el = waitShortForAny(signUpLinkXpath, signUpLinkId, signUpLinkText);
        if (el == null) {
            throw new NoSuchElementException("Unable to find Sign up link on Shopify home.");
        }
        el.click();
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(7));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.elementToBeClickable(by));
            } catch (TimeoutException ignored) { }
        }
        return null;
    }
}

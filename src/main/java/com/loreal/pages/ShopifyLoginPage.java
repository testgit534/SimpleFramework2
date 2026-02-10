package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopifyLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators from Browser-use JSON with fallbacks
    private final By emailXpath = By.xpath("html/body/div[3]/div[2]/div/div[2]/div/div[1]/form/div[2]/input");
    private final By emailId = By.id("customer_email");

    private final By passwordXpath = By.xpath("html/body/div[3]/div[2]/div/div[2]/div/div[1]/form/div[3]/input");
    private final By passwordId = By.id("customer_password");

    private final By signInXpath = By.xpath("html/body/div[3]/div[2]/div/div[2]/div/div[1]/form/div[5]/input");
    private final By signInCss = By.cssSelector("input.button[type='submit']");

    public ShopifyLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/account/login"),
                ExpectedConditions.visibilityOfElementLocated(emailId),
                ExpectedConditions.visibilityOfElementLocated(emailXpath)
        ));
    }

    public void enterEmail(String email) {
        WebElement em = findVisible(emailXpath, emailId);
        em.clear();
        em.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement pw = findVisible(passwordXpath, passwordId);
        pw.clear();
        pw.sendKeys(password);
    }

    public void submitLogin() {
        WebElement btn = findClickable(signInXpath, signInCss);
        btn.click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        submitLogin();
    }

    private WebElement findVisible(By... candidates) {
        for (By by : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(7))
                        .until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to find visible element using fallback locators.");
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

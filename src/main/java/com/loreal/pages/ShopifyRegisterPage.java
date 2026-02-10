package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopifyRegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Page readiness
    private final By pageBody = By.tagName("body");

    // Fields (prefer xpaths from Browser-use JSON, then ids/css)
    private final By firstNameXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[2]/input");
    private final By lastNameXpath  = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[3]/input");
    private final By emailXpath     = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[4]/input");
    private final By passwordXpath  = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[5]/input");
    private final By submitXpath    = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[6]/input");

    private final By firstNameId = By.id("first_name");
    private final By lastNameId  = By.id("last_name");
    private final By emailId     = By.id("email");
    private final By passwordId  = By.id("password");
    private final By submitCss   = By.cssSelector("form input[type='submit'][value='Create']");

    // Captcha / Challenge indicators (primary from JSON + robust fallbacks)
    private final By captchaDivJsonXpath = By.xpath("html/body/div[5]/div[2]");
    private final By challengeGeneric = By.cssSelector("#shopify-challenge-body, #shopify-challenge-token, #challenge, div.shopify-challenge__container, div[id*='challenge']");
    private final By captchaIframe = By.cssSelector("iframe[src*='challenge'], iframe[src*='captcha'], iframe[title*='captcha' i]");

    public ShopifyRegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/account/register"),
                ExpectedConditions.visibilityOfElementLocated(pageBody)
        ));
        // Ensure a key field is present
        waitShortForAny(firstNameXpath, firstNameId);
    }

    public void fillForm(String first, String last, String email, String password) {
        WebElement f = findVisibleWithFallback(firstNameXpath, firstNameId);
        f.clear(); f.sendKeys(first);

        WebElement l = findVisibleWithFallback(lastNameXpath, lastNameId);
        l.clear(); l.sendKeys(last);

        WebElement e = findVisibleWithFallback(emailXpath, emailId);
        e.clear(); e.sendKeys(email);

        WebElement p = findVisibleWithFallback(passwordXpath, passwordId);
        p.clear(); p.sendKeys(password);
    }

    public void submitCreate() {
        WebElement btn = findClickableWithFallback(submitXpath, submitCss);
        btn.click();
    }

    public boolean isCaptchaDisplayed() {
        try {
            // Try visible challenge containers or iframe
            if (isVisible(captchaDivJsonXpath) || isVisible(challengeGeneric) || isVisible(captchaIframe)) {
                return true;
            }
        } catch (Exception ignored) { }
        return false;
    }

    public boolean waitForCaptcha(Duration timeout) {
        try {
            WebDriverWait w = new WebDriverWait(driver, timeout);
            return w.until(d -> isCaptchaDisplayed());
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isOnRegisterPage() {
        return driver.getCurrentUrl().contains("/account/register");
    }

    public boolean isCreateButtonVisible() {
        try {
            return waitShortForAny(submitXpath, submitCss).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isVisible(By by) {
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(by));
            return el != null && el.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement waitShortForAny(By... locators) {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        for (By by : locators) {
            try {
                return shortWait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Expected element was not found using provided locators.");
    }

    private WebElement findVisibleWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate element using provided fallback locators.");
    }

    private WebElement findClickableWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(locator));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate clickable element using provided fallback locators.");
    }
}

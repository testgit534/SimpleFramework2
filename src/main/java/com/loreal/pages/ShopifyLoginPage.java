package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyLoginPage {

    private static final Logger log = Logger.getLogger(ShopifyLoginPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    // From Browser-use JSON for Login page
    private final By emailXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/div[1]/form/div[2]/input");
    private final By emailId = By.id("customer_email");

    private final By passwordXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/div[1]/form/div[3]/input");
    private final By passwordId = By.id("customer_password");

    private final By signInButtonXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/div[1]/form/div[5]/input");
    private final By signInButtonCss = By.cssSelector("form input.button[type='submit'][value='Sign In']");

    // Captcha/challenge indicators (same as Register page)
    private final By challengeIframeSrc = By.cssSelector("iframe[src*='challenge'], iframe[src*='captcha']");
    private final By challengeContainerIdContains = By.cssSelector("[id*='challenge'], [class*='challenge'], [id*='captcha'], [class*='captcha']");
    private final By challengeDialogRole = By.cssSelector("[role='dialog']");

    public ShopifyLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for Account Login page to be ready.");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/account/login"),
                ExpectedConditions.visibilityOfElementLocated(emailXpath),
                ExpectedConditions.visibilityOfElementLocated(emailId)
        ));
    }

    public void enterEmail(String email) {
        WebElement el = findVisible(emailXpath, emailId);
        el.clear();
        el.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement el = findVisible(passwordXpath, passwordId);
        el.clear();
        el.sendKeys(password);
    }

    public void submitSignIn() {
        log.info("Submitting Sign In form.");
        WebElement btn = findClickable(signInButtonXpath, signInButtonCss);
        btn.click();
    }

    public boolean waitForCaptchaOverlayIfPresent() {
        log.info("Waiting briefly for captcha/challenge overlay after login submit.");
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        try {
            shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(challengeIframeSrc),
                    ExpectedConditions.visibilityOfElementLocated(challengeContainerIdContains),
                    ExpectedConditions.visibilityOfElementLocated(challengeDialogRole),
                    ExpectedConditions.urlContains("/challenge")
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isStillOnLoginPage() {
        try {
            return driver.getCurrentUrl().contains("/account/login") &&
                    (isVisible(emailXpath) || isVisible(emailId));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isVisible(By by) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
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

    private WebElement findClickable(By... candidates) {
        for (By by : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.elementToBeClickable(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Unable to locate clickable element using provided locators.");
    }
}

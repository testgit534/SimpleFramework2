package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyRegisterPage {

    private static final Logger log = Logger.getLogger(ShopifyRegisterPage.class.getName());

    private final WebDriver driver;
    private final WebDriverWait wait;

    // From Browser-use JSON for Create Account page
    private final By firstNameXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[2]/input");
    private final By firstNameId = By.id("first_name");

    private final By lastNameXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[3]/input");
    private final By lastNameId = By.id("last_name");

    private final By emailXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[4]/input");
    private final By emailId = By.id("email");

    private final By passwordXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[5]/input");
    private final By passwordId = By.id("password");

    private final By createButtonXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[6]/input");
    private final By createButtonCss = By.cssSelector("form input[type='submit'][value='Create']");

    // Generic captcha/challenge indicators (Shopify bot protection)
    private final By challengeIframeSrc = By.cssSelector("iframe[src*='challenge'], iframe[src*='captcha']");
    private final By challengeContainerIdContains = By.cssSelector("[id*='challenge'], [class*='challenge'], [id*='captcha'], [class*='captcha']");
    private final By challengeDialogRole = By.cssSelector("[role='dialog']");

    public ShopifyRegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for Create Account page to be ready.");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/account/register"),
                ExpectedConditions.visibilityOfElementLocated(firstNameXpath),
                ExpectedConditions.visibilityOfElementLocated(firstNameId)
        ));
    }

    public void enterFirstName(String first) {
        WebElement el = findVisible(firstNameXpath, firstNameId);
        el.clear();
        el.sendKeys(first);
    }

    public void enterLastName(String last) {
        WebElement el = findVisible(lastNameXpath, lastNameId);
        el.clear();
        el.sendKeys(last);
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

    public void submitCreate() {
        log.info("Submitting Create Account form.");
        WebElement btn = findClickable(createButtonXpath, createButtonCss);
        btn.click();
    }

    public boolean waitForCaptchaOverlayIfPresent() {
        log.info("Waiting briefly for captcha/challenge overlay after registration submit.");
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

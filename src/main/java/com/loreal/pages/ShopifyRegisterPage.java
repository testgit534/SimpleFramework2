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

    // Fields from Browser-use JSON (prefer XPaths, then IDs)
    private final By firstNameXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[2]/input");
    private final By lastNameXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[3]/input");
    private final By emailXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[4]/input");
    private final By passwordXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[5]/input");
    private final By submitXpath = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[6]/input");

    private final By firstNameId = By.id("first_name");
    private final By lastNameId = By.id("last_name");
    private final By emailId = By.id("email");
    private final By passwordId = By.id("password");

    // Captcha indicators (from JSON and robust fallbacks)
    private final By captchaDivJson = By.xpath("html/body/div[5]/div[2]");
    private final By captchaIframeTitle = By.cssSelector("iframe[title*='challenge' i], iframe[title*='captcha' i]");
    private final By captchaIframeSrc = By.cssSelector("iframe[src*='captcha' i]");
    private final By captchaContainerCommon = By.cssSelector("div[id*='captcha' i], div[class*='captcha' i], div.g-recaptcha, div.h-captcha");
    private final By recaptchaAnchor = By.id("recaptcha-anchor");

    public ShopifyRegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        log.info("Waiting for Shopify Create Account page.");
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(firstNameXpath),
                ExpectedConditions.visibilityOfElementLocated(firstNameId),
                ExpectedConditions.urlContains("/account/register"),
                ExpectedConditions.titleContains("Create Account")
        ));
    }

    private WebElement findVisible(By... candidates) {
        for (By by : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(7))
                        .until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (TimeoutException ignored) { }
        }
        throw new NoSuchElementException("Element not found using provided locators.");
    }

    public void fillFirstName(String firstName) {
        log.info("Filling First Name: " + firstName);
        WebElement el = findVisible(firstNameXpath, firstNameId);
        el.clear();
        el.sendKeys(firstName);
    }

    public void fillLastName(String lastName) {
        log.info("Filling Last Name: " + lastName);
        WebElement el = findVisible(lastNameXpath, lastNameId);
        el.clear();
        el.sendKeys(lastName);
    }

    public void fillEmail(String email) {
        log.info("Filling Email: " + email);
        WebElement el = findVisible(emailXpath, emailId);
        el.clear();
        el.sendKeys(email);
    }

    public void fillPassword(String password) {
        log.info("Filling Password.");
        WebElement el = findVisible(passwordXpath, passwordId);
        el.clear();
        el.sendKeys(password);
    }

    public void submit() {
        log.info("Submitting Create Account form.");
        WebElement submitBtn = findVisible(submitXpath);
        submitBtn.click();
    }

    public boolean isCaptchaPresent() {
        log.info("Checking for presence of Captcha challenge.");
        // Direct container check first
        if (!driver.findElements(captchaDivJson).isEmpty() && driver.findElement(captchaDivJson).isDisplayed()) {
            return true;
        }
        if (!driver.findElements(captchaContainerCommon).isEmpty() && driver.findElement(captchaContainerCommon).isDisplayed()) {
            return true;
        }
        // Iframe-based captcha detection
        try {
            for (By frameBy : new By[]{captchaIframeTitle, captchaIframeSrc}) {
                for (WebElement iframe : driver.findElements(frameBy)) {
                    if (iframe.isDisplayed()) {
                        driver.switchTo().frame(iframe);
                        try {
                            if (!driver.findElements(recaptchaAnchor).isEmpty() && driver.findElement(recaptchaAnchor).isDisplayed()) {
                                driver.switchTo().defaultContent();
                                return true;
                            }
                            // If anchor not found, assume captcha iframe visible is sufficient
                            driver.switchTo().defaultContent();
                            return true;
                        } catch (Exception e) {
                            driver.switchTo().defaultContent();
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ignored) { 
            try { driver.switchTo().defaultContent(); } catch (Exception e) { }
        }
        return false;
    }

    public boolean isStillOnRegisterPage() {
        try {
            return driver.getCurrentUrl().contains("/account/register") || 
                   !driver.findElements(firstNameId).isEmpty() ||
                   !driver.findElements(firstNameXpath).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}

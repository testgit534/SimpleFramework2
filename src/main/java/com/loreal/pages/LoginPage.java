package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Primary locators from Browser-use JSON (xpath), with fallbacks (css/id)
    private By usernameFieldXpath = By.xpath("html/body/div[1]/div/div[2]/div[1]/div/div/form/div[1]/input");
    private By passwordFieldXpath = By.xpath("html/body/div[1]/div/div[2]/div[1]/div/div/form/div[2]/input");
    private By loginButtonXpath = By.xpath("html/body/div[1]/div/div[2]/div[1]/div/div/form/input");

    private By usernameFieldDataTest = By.cssSelector("input[data-test='username']");
    private By passwordFieldDataTest = By.cssSelector("input[data-test='password']");
    private By loginButtonDataTest = By.cssSelector("input[data-test='login-button']");

    // Existing locators (kept for compatibility with existing methods)
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");

    // Error message locator
    private By errorMessage = By.cssSelector("[data-test='error']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Existing Actions (kept as-is for backward compatibility)
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Convenience method (best practice)
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    // New/Enhanced Methods

    // Wait until login page is ready
    public void waitForLoginPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(usernameFieldXpath),
                ExpectedConditions.visibilityOfElementLocated(usernameFieldDataTest),
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        ));
    }

    // Attempt to find element using provided locators in order
    private WebElement findWithFallback(By... candidates) {
        for (By locator : candidates) {
            try {
                return new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException ignored) {
                // try next
            }
        }
        throw new NoSuchElementException("Unable to locate element using provided fallback locators.");
    }

    // Preferred login using JSON xpaths first, then fallbacks
    public void loginUsingPreferredLocators(String username, String password) {
        WebElement user = findWithFallback(usernameFieldXpath, usernameFieldDataTest, usernameField);
        user.clear();
        user.sendKeys(username);

        WebElement pass = findWithFallback(passwordFieldXpath, passwordFieldDataTest, passwordField);
        pass.clear();
        pass.sendKeys(password);

        WebElement btn = findWithFallback(loginButtonXpath, loginButtonDataTest, loginButton);
        btn.click();
    }

    // Retrieve the error text, waiting until visible
    public String getErrorMessageText() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return errorEl.getText().trim();
    }

    public boolean isErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}

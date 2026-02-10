package com.loreal.pages;

import com.loreal.utils.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ---------- Locator Resolver ----------
    private By getLocator(String key) {
        String value = PropertyReader.get(key);

        if (value.startsWith("css:")) {
            return By.cssSelector(value.replace("css:", ""));
        }
        return By.xpath(value);
    }

    // ---------- Locators ----------
    private By usernameXpath = getLocator("login.username.xpath");
    private By passwordXpath = getLocator("login.password.xpath");
    private By loginButtonXpath = getLocator("login.button.xpath");

    private By usernameDataTest = getLocator("login.username.datatest");
    private By passwordDataTest = getLocator("login.password.datatest");
    private By loginButtonDataTest = getLocator("login.button.datatest");

    private By errorMessage = getLocator("login.error.message");

    // ---------- Fallback Finder ----------
    private WebElement findWithFallback(By... locators) {
        for (By locator : locators) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (TimeoutException ignored) {
            }
        }
        throw new NoSuchElementException("Element not found using provided locators");
    }

    // ---------- Actions ----------
    public void login(String username, String password) {

        WebElement user = findWithFallback(usernameXpath, usernameDataTest);
        user.clear();
        user.sendKeys(username);

        WebElement pass = findWithFallback(passwordXpath, passwordDataTest);
        pass.clear();
        pass.sendKeys(password);

        WebElement loginBtn = findWithFallback(loginButtonXpath, loginButtonDataTest);
        loginBtn.click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage))
                   .getText().trim();
    }

    public boolean isErrorDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

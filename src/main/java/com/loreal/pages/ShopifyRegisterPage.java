package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ShopifyRegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators (from Browser-use JSON first, with robust fallbacks)
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

    public ShopifyRegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForPage() {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/account/register"),
                ExpectedConditions.visibilityOfElementLocated(firstNameId),
                ExpectedConditions.visibilityOfElementLocated(firstNameXpath)
        ));
    }

    public void fillForm(String firstName, String lastName, String email, String password) {
        WebElement fn = findVisible(firstNameXpath, firstNameId);
        fn.clear();
        fn.sendKeys(firstName);

        WebElement ln = findVisible(lastNameXpath, lastNameId);
        ln.clear();
        ln.sendKeys(lastName);

        WebElement em = findVisible(emailXpath, emailId);
        em.clear();
        em.sendKeys(email);

        WebElement pw = findVisible(passwordXpath, passwordId);
        pw.clear();
        pw.sendKeys(password);
    }

    public void submitCreate() {
        WebElement btn = findClickable(createButtonXpath, createButtonCss);
        btn.click();
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

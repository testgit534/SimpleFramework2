package com.loreal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegistrationPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for registration page
    private By signUpLink = By.xpath("html/body/header/div[1]/div[2]/nav/a[4]");
    private By emailField = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[4]/input");
    private By passwordField = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[5]/input");
    private By createButton = By.xpath("html/body/div[4]/div[2]/div/div[2]/div/form/div[6]/input");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToSignUp() {
        WebElement signUp = wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        signUp.click();
    }

    public void enterEmail(String email) {
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickCreateAccount() {
        WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(createButton));
        createBtn.click();
    }
}

package com.loreal.test;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import pages.LoginPage;

public class InvalidLoginTest extends BaseTest {

    @Test
    public void shouldDisplayErrorMessageForInvalidCredentials() {
        Reporter.log("Step 1: Navigate to SauceDemo login page and validate page load.", true);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginPage();
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Unexpected page title on login page.");

        Reporter.log("Step 2: Enter invalid credentials and attempt to login.", true);
        loginPage.loginUsingPreferredLocators("invalid_user", "wrong_password");

        Reporter.log("Step 3: Validate error message is displayed.", true);
        Assert.assertTrue(loginPage.isErrorDisplayed(), "Expected error message was not displayed.");

        String expectedError = "Epic sadface: Username and password do not match any user in this service";
        String actualError = loginPage.getErrorMessageText();
        Reporter.log("Captured error message: " + actualError, true);
        Assert.assertEquals(actualError, expectedError, "Error message text mismatch.");

        Reporter.log("Step 4: Validate user remains on the login page after failed login.", true);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("saucedemo.com"), "Current URL does not contain saucedemo.com domain.");
        Assert.assertFalse(currentUrl.contains("inventory"), "User should not be redirected to inventory page on failed login.");

        Reporter.log("Test Completed: Invalid login error message validated successfully.", true);
    }
}

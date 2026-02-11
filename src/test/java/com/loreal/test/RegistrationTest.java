package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.RegistrationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTest extends BaseTest {

    @Test
    public void testUserRegistration() {
        RegistrationPage registrationPage = new RegistrationPage(driver);

        // Navigate to Sign Up page
        registrationPage.navigateToSignUp();
        logger.info("Navigated to Sign Up page.");

        // Entering user details for registration
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "SecurePassword123";

        registrationPage.enterEmail(email);
        logger.info("Entered email: " + email);

        registrationPage.enterPassword(password);
        logger.info("Entered password.");

        // Clicking Create Account button
        registrationPage.clickCreateAccount();
        logger.info("Clicked Create Account button.");

        // Handling CAPTCHA if any by displaying a warning for manual intervention
        By captchaWarningMessage = By.cssSelector("div.captcha-challenge");
        boolean isCaptchaDisplayed = driver.findElements(captchaWarningMessage).size() > 0;
        Assert.assertFalse(isCaptchaDisplayed, "CAPTCHA is preventing automated registration flow.");

        // Assert final expected state: user should be navigated to account confirmation or dashboard page
        // Depending on the application flow
        By accountConfirmLocator = By.xpath("//h1[contains(text(),'Account Confirmation')]");
        By userDashboardLocator = By.xpath("//h1[contains(text(),'Welcome John')]");

        WebElement element = wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(accountConfirmLocator),
                ExpectedConditions.visibilityOfElementLocated(userDashboardLocator)
        ));

        Assert.assertTrue(element.isDisplayed(), "User registration or login not successful.");
    }
}

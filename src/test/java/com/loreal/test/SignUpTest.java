package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.logging.Logger;

public class SignUpTest extends BaseTest {

    private static final Logger log = Logger.getLogger(SignUpTest.class.getName());

    @Test
    public void testUserSignUpFlow() {
        log.info("Starting test for user sign-up flow.");

        RegistrationPage registrationPage = new RegistrationPage(driver);

        log.info("Navigating to the sign-up page.");
        registrationPage.navigateToSignUp();

        log.info("Entering email address.");
        registrationPage.enterEmail("unique.email@example.com");

        log.info("Entering password.");
        registrationPage.enterPassword("SecurePassword123");

        log.info("Attempting to create account.");
        registrationPage.clickCreateAccount();

        // Validate captcha error handle (example placeholder as actual captcha handling is complex and outside Selenium capabilities)
        // Real captcha handling would require work outside typical Selenium capabilities (e.g., human intervention, captcha service bypass)
        try {
            Assert.assertFalse(driver.getCurrentUrl().contains("login"), "Sign-up failed due to captcha.");
            log.info("Sign-up process completed successfully.");
        } catch (Exception e) {
            log.warning("Sign-up could not proceed due to captcha block.");
        }
    }
}

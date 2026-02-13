package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ShopifyRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;
import java.util.logging.Logger;

public class ShopifyAccountCreationCaptchaTest extends BaseTest {

    private static final Logger log = Logger.getLogger(ShopifyAccountCreationCaptchaTest.class.getName());

    @Test
    public void testAccountCreationBlockedByCaptcha() {
        // Navigate to Shopify Sauce Demo Register page as per Browser-use flow
        log.info("Navigating to Shopify Sauce Demo registration page.");
        driver.navigate().to("https://sauce-demo.myshopify.com/account/register");

        ShopifyRegisterPage registerPage = new ShopifyRegisterPage(driver);

        log.info("Waiting for 'Create Account' page to be ready.");
        registerPage.waitForPage();

        // Input details from Browser-use run
        log.info("Filling in account details: First Name, Last Name, Email, Password.");
        registerPage.enterFirstName("John");
        registerPage.enterLastName("Doe");

        // Use a stable email (as in Browser-use) or a unique one to avoid conflicts; captcha blocks anyway
        String email = "john.doe@example.com";
        // Alternatively: String email = "john.doe+" + UUID.randomUUID().toString().substring(0,8) + "@example.com";
        registerPage.enterEmail(email);

        registerPage.enterPassword("SecurePassword123");

        log.info("Submitting the account creation form.");
        registerPage.clickCreate();

        log.info("Waiting for CAPTCHA or challenge indication after submit.");
        boolean captchaOrChallengeShown = registerPage.waitForCaptchaOrChallenge();

        // Assertions aligned with Browser-use result: creation blocked by captcha, still on register/challenge context
        log.info("Validating that account creation is blocked by CAPTCHA.");
        Assert.assertTrue(captchaOrChallengeShown, "Expected CAPTCHA/challenge to appear after submitting registration.");

        log.info("Validating that we remain on register or challenge URL context.");
        Assert.assertTrue(registerPage.isStillOnRegisterOrChallenge(),
                "Expected to remain on /account/register or be redirected to a /challenge URL.");

        log.info("Test completed: CAPTCHA prevented automated account creation as observed in Browser-use execution.");
    }
}

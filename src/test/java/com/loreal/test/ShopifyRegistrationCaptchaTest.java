package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ShopifyHomePage;
import com.loreal.pages.ShopifyRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyRegistrationCaptchaTest extends BaseTest {

    private static final Logger log = Logger.getLogger(ShopifyRegistrationCaptchaTest.class.getName());

    @Test
    public void testRegistrationTriggersCaptchaOnShopifyStore() {
        // Navigate to Sauce Demo Shopify store (overriding BaseTest default URL)
        log.info("Navigating to Sauce Demo Shopify store home page.");
        driver.get("https://sauce-demo.myshopify.com/");

        ShopifyHomePage home = new ShopifyHomePage(driver);
        home.waitForHome();
        log.info("Home page loaded. Clicking 'Sign up' link to go to Create Account page.");
        home.clickSignUp();

        ShopifyRegisterPage registerPage = new ShopifyRegisterPage(driver);
        registerPage.waitForPage();
        log.info("Create Account page loaded. Filling the account creation form.");

        String uniqueEmail = "john.doe+" + System.currentTimeMillis() + "@example.com";
        registerPage.fillForm("John", "Doe", uniqueEmail, "SecurePassword123");

        log.info("Submitting the Create Account form.");
        registerPage.submitCreate();

        log.info("Waiting for captcha challenge to appear after submission.");
        boolean captchaVisible = registerPage.waitForCaptcha(Duration.ofSeconds(15));
        boolean stillOnRegister = registerPage.isOnRegisterPage();

        // Assertions aligned with Browser-use results indicating captcha prevented submission
        log.info("Verifying that a captcha challenge is displayed and the user remains on the register page.");
        Assert.assertTrue(captchaVisible, "Expected captcha challenge to be displayed after submitting the register form.");
        Assert.assertTrue(stillOnRegister, "Expected to remain on the registration page due to captcha challenge.");

        // Optional additional check: Create button should still be visible because submission is blocked
        Assert.assertTrue(registerPage.isCreateButtonVisible(),
                "Expected 'Create' button to remain visible since registration is blocked by captcha.");

        log.info("Validation complete: Captcha detected and registration flow appropriately blocked as per execution results.");
    }
}

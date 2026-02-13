package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ShopifyHomePage;
import com.loreal.pages.ShopifyRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class ShopifyAccountCreationCaptchaTest extends BaseTest {

    private static final Logger log = Logger.getLogger(ShopifyAccountCreationCaptchaTest.class.getName());

    @Test
    public void shopifyAccountRegistrationCaptchaBlocksFlow() {
        log.info("Navigating to Shopify demo store home.");
        driver.get("https://sauce-demo.myshopify.com/");

        ShopifyHomePage home = new ShopifyHomePage(driver);
        home.waitForHeader();

        log.info("Clicking Sign up to open Create Account page.");
        home.clickSignUp();

        ShopifyRegisterPage register = new ShopifyRegisterPage(driver);
        register.waitForPage();

        log.info("Filling account details from Browser-use execution results.");
        register.fillFirstName("John");
        register.fillLastName("Doe");
        register.fillEmail("john.doe@example.com");
        register.fillPassword("SecurePassword123");

        log.info("Submitting the Create Account form.");
        register.submit();

        log.info("Validating captcha presence as per observed Browser-use results.");
        boolean captchaPresent = register.isCaptchaPresent();
        boolean stillOnRegister = register.isStillOnRegisterPage();

        log.info("Asserting that captcha blocked automated account creation and user remains on register page.");
        Assert.assertTrue(captchaPresent, "Expected a Captcha challenge to be present after submitting the form.");
        Assert.assertTrue(stillOnRegister, "Expected to remain on the Create Account page due to Captcha challenge.");
    }
}

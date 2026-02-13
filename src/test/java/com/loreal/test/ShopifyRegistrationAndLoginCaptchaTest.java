package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ShopifyHomePage;
import com.loreal.pages.ShopifyLoginPage;
import com.loreal.pages.ShopifyRegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class ShopifyRegistrationAndLoginCaptchaTest extends BaseTest {

    private static final Logger log = Logger.getLogger(ShopifyRegistrationAndLoginCaptchaTest.class.getName());

    @Test
    public void registerAndLogin_encountersCaptcha_onSauceDemoShopify() {
        // Navigate to the Shopify site (BaseTest opens saucedemo.com by default)
        String baseUrl = "https://sauce-demo.myshopify.com/";
        log.info("Navigating to Shopify site: " + baseUrl);
        driver.navigate().to(baseUrl);

        // Home header validations
        ShopifyHomePage home = new ShopifyHomePage(driver);
        home.waitForHeader();

        // Start registration flow
        log.info("Starting user registration flow via header 'Sign up'.");
        home.clickSignUp();

        ShopifyRegisterPage register = new ShopifyRegisterPage(driver);
        register.waitForPage();

        log.info("Filling registration form with sample data from Browser-use flow.");
        register.enterFirstName("John");
        register.enterLastName("Doe");
        register.enterEmail("john.doe@example.com");
        register.enterPassword("SecurePassword123");
        register.submitCreate();

        // Validate captcha encountered after registration submit (as per Browser-use results)
        boolean regCaptcha = register.waitForCaptchaOverlayIfPresent();
        log.info("Captcha overlay detected after registration submit: " + regCaptcha);
        Assert.assertTrue(regCaptcha, "Expected captcha overlay to appear after registration submit, aligned with Browser-use results.");

        // Proceed to login via header (Browser-use eventually navigated to Login and hit captcha again)
        log.info("Navigating to Login page via header.");
        ShopifyHomePage headerContext = new ShopifyHomePage(driver);
        headerContext.waitForHeader();
        headerContext.clickLogin();

        ShopifyLoginPage login = new ShopifyLoginPage(driver);
        login.waitForPage();

        log.info("Filling login credentials and submitting Sign In.");
        login.enterEmail("john.doe@example.com");
        login.enterPassword("SecurePassword123");
        login.submitSignIn();

        // Validate captcha encountered on login submit and that we remain on login page
        boolean loginCaptcha = login.waitForCaptchaOverlayIfPresent();
        log.info("Captcha overlay detected after login submit: " + loginCaptcha);
        Assert.assertTrue(loginCaptcha, "Expected captcha overlay to appear after login submit, aligned with Browser-use results.");

        boolean stillOnLogin = login.isStillOnLoginPage();
        log.info("Still on Login page after captcha challenge: " + stillOnLogin);
        Assert.assertTrue(stillOnLogin, "User should remain on login page when captcha blocks automated sign-in.");
    }
}

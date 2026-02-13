package com.loreal.test;

import base.BaseTest;
import com.shopify.pages.ShopifyCaptchaOverlay;
import com.shopify.pages.ShopifyHeader;
import com.shopify.pages.ShopifyLoginPage;
import com.shopify.pages.ShopifyRegisterPage;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.logging.Logger;

public class ShopifyAccountFlowTest extends BaseTest {

    private static final Logger log = Logger.getLogger(ShopifyAccountFlowTest.class.getName());

    @Test
    public void testRegisterThenLoginShowsCaptchaOnShopifyDemo() {
        // Navigate to Shopify demo storefront
        log.info("Navigating to Shopify demo storefront.");
        driver.navigate().to("https://sauce-demo.myshopify.com/");
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("sauce demo"), "Expected Shopify demo title to contain 'Sauce Demo'.");

        // Header actions - Open Sign Up/Register
        ShopifyHeader header = new ShopifyHeader(driver);
        header.waitForHeader();
        header.clickSignUp();

        // Register page - fill and submit
        ShopifyRegisterPage registerPage = new ShopifyRegisterPage(driver);
        registerPage.waitForPage();
        log.info("Filling Create Account form with test data.");
        registerPage.fillForm("John", "Doe", "john.doe@example.com", "SecurePassword123");
        log.info("Submitting Create Account form.");
        registerPage.submitCreate();

        // Expect captcha challenge to appear after submit
        ShopifyCaptchaOverlay captcha = new ShopifyCaptchaOverlay(driver);
        boolean captchaAfterRegister = captcha.isVisible(Duration.ofSeconds(12));
        log.info("Captcha visible after registration submit: " + captchaAfterRegister);
        Assert.assertTrue(captchaAfterRegister, "Expected captcha to appear after registration submit on Shopify demo.");

        // Navigate to Login
        log.info("Navigating to Login page from header.");
        try {
            header.clickLogin();
        } catch (TimeoutException e) {
            log.info("Fallback: direct navigation to login page due to potential overlay.");
            driver.navigate().to("https://sauce-demo.myshopify.com/account/login");
        }

        // Login page - attempt to login
        ShopifyLoginPage loginPage = new ShopifyLoginPage(driver);
        loginPage.waitForPage();
        log.info("Attempting to log in with test credentials.");
        loginPage.login("john.doe@example.com", "SecurePassword123");

        // Expect captcha challenge again on login submit
        boolean captchaAfterLogin = captcha.isVisible(Duration.ofSeconds(12));
        log.info("Captcha visible after login submit: " + captchaAfterLogin);
        Assert.assertTrue(captchaAfterLogin, "Expected captcha to appear after login submit on Shopify demo.");

        // Final validation: ensure we did not navigate past login due to captcha
        String currentUrl = driver.getCurrentUrl();
        boolean stillOnAuthFlow = currentUrl.contains("/account/login") || currentUrl.contains("/account/register") || currentUrl.contains("challenge");
        Assert.assertTrue(stillOnAuthFlow, "User should remain on authentication flow due to captcha challenge. Current URL: " + currentUrl);
    }
}

package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class SauceDemoE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(SauceDemoE2ETest.class.getName());

    @Test
    public void loginNavigateToCart_EndToEnd() {
        log.info("Starting end-to-end test: Login -> Inventory -> Cart");

        // Step 1: Login Page
        LoginPage loginPage = new LoginPage(driver);
        log.info("Waiting for login page to be ready.");
        loginPage.waitForLoginPage();

        log.info("Logging in using preferred locators (XPath first, then fallbacks).");
        loginPage.loginUsingPreferredLocators("standard_user", "secret_sauce");

        // Step 2: Inventory Page
        InventoryPage inventoryPage = new InventoryPage(driver);
        log.info("Waiting for Inventory page to load.");
        inventoryPage.waitForPage();

        String inventoryTitle = inventoryPage.getTitleText();
        log.info("Asserting Inventory title. Actual: " + inventoryTitle);
        Assert.assertEquals(inventoryTitle, "Products", "Inventory page title should be 'Products'.");

        // Step 3: Navigate to Cart
        log.info("Clicking on the cart link.");
        inventoryPage.clickCart();

        // Step 4: Cart Page
        CartPage cartPage = new CartPage(driver);
        log.info("Waiting for Cart page to load.");
        cartPage.waitForPage();

        String cartTitle = cartPage.getTitleText();
        log.info("Asserting Cart page title. Actual: " + cartTitle);
        Assert.assertEquals(cartTitle, "Your Cart", "Cart page title should be 'Your Cart'.");

        log.info("Asserting URL contains 'cart.html'.");
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"), "URL should contain 'cart.html'.");

        log.info("End-to-end test completed successfully.");
    }
}

package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class SauceDemoE2ETest extends BaseTest {

    private static final Logger logger = Logger.getLogger(SauceDemoE2ETest.class.getName());

    @Test
    public void loginNavigateToCart_e2e() {
        logger.info("Starting E2E test: Login using preferred (xpath-first) locators and navigate to Cart.");

        // Login Page
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Waiting for Login page to be ready.");
        loginPage.waitForLoginPage();

        logger.info("Performing login using preferred locators (xpath -> css -> id).");
        loginPage.loginUsingPreferredLocators("standard_user", "secret_sauce");

        // Inventory Page
        InventoryPage inventoryPage = new InventoryPage(driver);
        logger.info("Waiting for Inventory page to load.");
        inventoryPage.waitForPage();

        String currentUrl = driver.getCurrentUrl();
        logger.info("Asserting Inventory page URL and title.");
        Assert.assertTrue(currentUrl.contains("/inventory.html"),
                "Expected to navigate to inventory page, but URL was: " + currentUrl);
        Assert.assertEquals(inventoryPage.getTitleText(), "Products",
                "Inventory page title mismatch.");

        // Navigate to Cart
        logger.info("Clicking on Cart link using robust fallback locators.");
        inventoryPage.clickCart();

        // Cart Page
        CartPage cartPage = new CartPage(driver);
        logger.info("Waiting for Cart page to load.");
        cartPage.waitForPage();

        String cartUrl = driver.getCurrentUrl();
        logger.info("Asserting Cart page URL and title.");
        Assert.assertTrue(cartUrl.contains("/cart.html"),
                "Expected to navigate to cart page, but URL was: " + cartUrl);
        Assert.assertEquals(cartPage.getTitleText(), "Your Cart",
                "Cart page title mismatch.");

        logger.info("E2E test completed successfully: User logged in and navigated to Cart.");
    }
}

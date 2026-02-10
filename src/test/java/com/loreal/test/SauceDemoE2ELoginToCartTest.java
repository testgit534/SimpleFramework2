package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class SauceDemoE2ELoginToCartTest extends BaseTest {

    private static final Logger log = Logger.getLogger(SauceDemoE2ELoginToCartTest.class.getName());

    @Test
    public void verifySuccessfulLoginAndNavigateToCart() {
        log.info("Step 1: Wait for Login page to be ready.");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginPage();

        log.info("Step 2: Perform login using preferred locators (xpath-first).");
        String username = "standard_user";
        String password = "secret_sauce";
        loginPage.loginUsingPreferredLocators(username, password);

        log.info("Step 3: Wait for Inventory page to load and validate the title.");
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitForPage();
        String actualInventoryTitle = inventoryPage.getTitleText();
        log.info("Asserting Inventory page title. Expected: 'Products', Actual: '" + actualInventoryTitle + "'");
        Assert.assertEquals(actualInventoryTitle, "Products", "Inventory page title mismatch after login.");
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory"), "URL does not indicate inventory page after login.");

        log.info("Step 4: Navigate to the Cart page via cart icon/link.");
        inventoryPage.clickCart();

        log.info("Step 5: Wait for Cart page and validate the title.");
        CartPage cartPage = new CartPage(driver);
        cartPage.waitForPage();
        String actualCartTitle = cartPage.getTitleText();
        log.info("Asserting Cart page title. Expected: 'Your Cart', Actual: '" + actualCartTitle + "'");
        Assert.assertEquals(actualCartTitle, "Your Cart", "Cart page title mismatch after navigation.");

        log.info("E2E flow completed: Login successful, Inventory page validated, navigated to Cart page successfully.");
    }
}

package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginAndOpenCartTest extends BaseTest {

    @Test
    public void testLoginAndOpenCart() {
        Reporter.log("Step 1: Navigate to SauceDemo login page and wait until ready", true);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginPage();

        Reporter.log("Step 2: Perform login using preferred locators (from Browser-use JSON) with valid credentials", true);
        loginPage.loginUsingPreferredLocators("standard_user", "secret_sauce");

        Reporter.log("Step 3: Wait for Inventory page to load and validate successful navigation", true);
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitForPage();

        String currentUrl = driver.getCurrentUrl();
        Reporter.log("Current URL after login: " + currentUrl, true);
        Assert.assertTrue(currentUrl.contains("/inventory.html"),
                "Expected to be on inventory page after login. Actual URL: " + currentUrl);

        String inventoryTitle = inventoryPage.getTitleText();
        Reporter.log("Inventory page title: " + inventoryTitle, true);
        Assert.assertEquals(inventoryTitle, "Products", "Inventory page title mismatch.");

        Reporter.log("Step 4: Click on the cart icon/link from the Inventory page", true);
        inventoryPage.clickCart();

        Reporter.log("Step 5: Wait for Cart page and validate navigation and title", true);
        CartPage cartPage = new CartPage(driver);
        cartPage.waitForPage();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/cart.html"));
        String cartUrl = driver.getCurrentUrl();
        Reporter.log("Current URL on cart page: " + cartUrl, true);
        Assert.assertTrue(cartUrl.contains("/cart.html"),
                "Expected to be on cart page after clicking cart. Actual URL: " + cartUrl);

        String cartTitle = cartPage.getTitleText();
        Reporter.log("Cart page title: " + cartTitle, true);
        Assert.assertEquals(cartTitle, "Your Cart", "Cart page title mismatch.");

        Reporter.log("End-to-end flow validated: Login successful and cart page opened.", true);
    }
}

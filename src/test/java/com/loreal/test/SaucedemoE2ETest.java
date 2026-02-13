package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class SaucedemoE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(SaucedemoE2ETest.class.getName());

    @Test
    public void endToEnd_AddProductToCart_And_VerifyInCart() {
        String username = "standard_user";
        String password = "secret_sauce";
        String productName = "Sauce Labs Backpack";

        log.info("Step 1: Wait for Login page and perform login using preferred locators.");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginPage();
        loginPage.loginUsingPreferredLocators(username, password);

        log.info("Step 2: Wait for Inventory page to load and assert title.");
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.waitForPage();
        String inventoryTitle = inventoryPage.getTitleText();
        log.info("Inventory Title: " + inventoryTitle);
        Assert.assertEquals(inventoryTitle, "Products", "Expected to be on Products page after login.");

        log.info("Step 3: Add product to cart using robust locators and verify cart badge increments.");
        inventoryPage.addProductToCartByName(productName);

        int badgeCount = inventoryPage.getCartBadgeCount();
        log.info("Cart badge count after adding item: " + badgeCount);
        Assert.assertTrue(badgeCount >= 1, "Cart badge should be at least 1 after adding an item.");

        log.info("Step 4: Navigate to Cart and assert 'Your Cart' page loads.");
        inventoryPage.clickCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.waitForPage();
        String cartTitle = cartPage.getTitleText();
        log.info("Cart Title: " + cartTitle);
        Assert.assertEquals(cartTitle, "Your Cart", "Expected to be on Your Cart page.");

        log.info("Step 5: Verify the added product is present in the cart with correct quantity.");
        Assert.assertTrue(cartPage.isItemPresent(productName), "Expected product to be present in cart: " + productName);
        int qty = cartPage.getItemQuantity(productName);
        log.info("Quantity for '" + productName + "' in cart: " + qty);
        Assert.assertEquals(qty, 1, "Expected quantity to be 1 for the added product.");
    }
}

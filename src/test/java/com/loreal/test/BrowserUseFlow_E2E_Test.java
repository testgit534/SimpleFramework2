package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CartPage;
import com.loreal.pages.InventoryPage;
import com.loreal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class BrowserUseFlow_E2E_Test extends BaseTest {

    private static final Logger log = Logger.getLogger(BrowserUseFlow_E2E_Test.class.getName());

    @Test
    public void completeFlowBasedOnBrowserUseResults() {
        // Step 1: Login (aligns with initial auth attempts in Browser-use results, leveraging XPath-first strategy)
        LoginPage loginPage = new LoginPage(driver);
        log.info("Waiting for login page to be ready.");
        loginPage.waitForLoginPage();

        log.info("Performing login using preferred locators (XPath -> data-test -> id).");
        loginPage.loginUsingPreferredLocators("standard_user", "secret_sauce");

        // Step 2: Inventory page verification
        InventoryPage inventoryPage = new InventoryPage(driver);
        log.info("Waiting for inventory (Products) page to load.");
        inventoryPage.waitForPage();

        String title = inventoryPage.getTitleText();
        log.info("Inventory page title text: " + title);
        Assert.assertEquals(title, "Products", "Expected to be on Products page after login.");

        // Step 3: Add a product to cart (simulate add-to-cart intent from Browser-use)
        String productName = "Sauce Labs Backpack";
        log.info("Adding product to cart: " + productName);
        inventoryPage.addProductToCartByName(productName);

        int badgeCount = inventoryPage.getCartBadgeCount();
        log.info("Cart badge count after adding item: " + badgeCount);
        Assert.assertTrue(badgeCount >= 1, "Cart badge should reflect at least 1 item after adding.");

        // Step 4: Open cart and verify cart page and item presence (final verification)
        log.info("Navigating to the cart page.");
        inventoryPage.clickCart();

        CartPage cartPage = new CartPage(driver);
        log.info("Waiting for cart page to load.");
        cartPage.waitForPage();

        String cartTitle = cartPage.getTitleText();
        log.info("Cart page title: " + cartTitle);
        Assert.assertEquals(cartTitle, "Your Cart", "Expected 'Your Cart' title to be visible.");

        log.info("Verifying item is listed in cart: " + productName);
        Assert.assertTrue(cartPage.isItemPresentByName(productName), "Expected product to be present in the cart.");

        int cartItems = cartPage.getCartItemsCount();
        log.info("Total items listed in cart: " + cartItems);
        Assert.assertTrue(cartItems >= 1, "Cart should list at least one item.");
    }
}

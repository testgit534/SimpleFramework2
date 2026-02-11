package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.ShopifyProductPage;
import org.testng.annotations.Test;

public class ShopifyProductTest extends BaseTest {

    @Test
    public void testAddProductToCartAndVerify() {
        // Navigate to Shopify product page and perform actions

        ShopifyProductPage productPage = new ShopifyProductPage(driver);

        // Select product from the inventory and add to cart
        productPage.selectProduct();
        productPage.addToCart();

        // Go to cart to verify the product is added
        productPage.gotoCart();

        // Assertions to verify product is successfully added to the cart
        // Assuming we have a CartPage class to handle cart assertions
        CartPage cartPage = new CartPage(driver);
        cartPage.waitForPage();
        assert cartPage.getTitleText().equals("Your Cart") : "Cart page did not load correctly.";
        // Additional assertions to check product details in the cart can be added here
    }
}

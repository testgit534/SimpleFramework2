package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.logging.Logger;

public class LoginTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LoginTest.class.getName());

    @Test
    public void testLoginFunctionality() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        log.info("Navigating to login page and attempting to login.");
        
        // Execute login action
        loginPage.loginUsingPreferredLocators("standard_user", "secret_sauce");

        log.info("Waiting for Inventory page to load after login.");
        
        // Wait until the inventory page is loaded
        inventoryPage.waitForPage();

        log.info("Verifying that the Inventory page title is displayed.");
        
        // Assert that the Inventory page is displayed by checking title
        String expectedTitle = "Products";
        String actualTitle = inventoryPage.getTitleText();
        Assert.assertEquals(actualTitle, expectedTitle, "Inventory page title does not match!");

        log.info("Login test completed successfully.");
    }
}

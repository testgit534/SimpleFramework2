package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccountCreationAndLoginTest extends BaseTest {

    @Test
    public void testAccountCreationAndLogin() {
        WebDriver driver = super.driver;
        
        // Initialize required page objects
        LorealHomePage homePage = new LorealHomePage(driver);
        RegistrationPage registrationPage = new RegistrationPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);
        
        // Step 1: Navigate to the home page and accept cookies if any
        homePage.waitForHomeHeader();
        homePage.acceptCookiesIfPresent();

        // Step 2: Navigate to the sign-up page
        registrationPage.navigateToSignUp();

        // Step 3: Enter new account details
        registrationPage.enterEmail("john.doe@example.com");
        registrationPage.enterPassword("SecurePassword123");
        registrationPage.clickCreateAccount();

        // [Note: Handling of captcha to be done here if possible, skipping due to lack of control over it]

        // Step 4: Navigate to Login page and verify
        driver.get("https://sauce-demo.myshopify.com/account/login");
        loginPage.waitForLoginPage();
        
        // Step 5: Login with created account credentials
        loginPage.enterUsername("john.doe@example.com");
        loginPage.enterPassword("SecurePassword123");
        loginPage.clickLogin();

        // [Note: Handle Captcha if possible, demonstrate completion for control]
        
        // Verify login by checking navigation to Inventory or Home Page could be done
        inventoryPage.waitForPage();
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"), "Login was not successful, Inventory page not loaded.");

        // Step 6: Optional verify if the cart opens successfully (after potential login)
        inventoryPage.clickCart();
        cartPage.waitForPage();
        Assert.assertEquals(cartPage.getTitleText(), "Your Cart", "Failed to open cart page correctly.");
    }
}

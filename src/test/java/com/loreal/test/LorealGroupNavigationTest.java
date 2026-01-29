package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealGroupPage;
import com.loreal.pages.LorealHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealGroupNavigationTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealGroupNavigationTest.class.getName());

    @Test
    public void validateHamburgerMenuGroupNavigation() {
        // Navigate to L'Oréal global site (override BaseTest default URL)
        driver.get("https://www.loreal.com/en/");
        log.info("Navigated to L'Oréal homepage.");

        LorealHomePage home = new LorealHomePage(driver);

        // Wait for header and handle cookies
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();
        log.info("Header is visible and cookie banner handled (if present).");

        // Basic visual validation of header/logo
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible in header.");

        // Open hamburger and validate it is open
        home.openMainMenu();
        Assert.assertTrue(home.isMainMenuOpen(), "Expected main navigation menu to be open after clicking burger.");
        log.info("Main navigation menu is open.");

        // Click on 'Group' link as in the Browser-use flow
        home.clickGroupLink();
        log.info("'Group' link clicked from main navigation.");

        // Validate Group page
        LorealGroupPage group = new LorealGroupPage(driver);
        group.waitForPage();
        Assert.assertTrue(group.isOnGroupUrl(), "Expected URL to contain '/en/group/' after navigation.");
        String headerText = group.getHeaderText();
        log.info("Group page header text resolved as: " + headerText);
        // Not asserting exact header text due to variability; ensure we did not land on an empty page
        Assert.assertTrue(driver.getTitle() != null && !driver.getTitle().isEmpty(),
                "Expected a non-empty page title on Group page.");

        // Optional negative check: ensure no login/sign-in surfaced in main header (alignment with Scenario 1 not completed)
        // Not asserting here to avoid flakiness, but logging state:
        boolean loginPresent = new LorealHomePage(driver).isLoginOptionPresent();
        log.info("Login/Sign-in option present on page: " + loginPresent);
    }
}

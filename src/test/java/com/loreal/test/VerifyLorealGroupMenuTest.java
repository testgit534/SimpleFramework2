package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class VerifyLorealGroupMenuTest extends BaseTest {

    private static final Logger log = Logger.getLogger(VerifyLorealGroupMenuTest.class.getName());

    @Test
    public void verifyHamburgerMenuAndGroupLinkExpansion() {
        // Navigate to L'Oréal global site (override BaseTest default start URL)
        log.info("Navigating to L'Oréal home page.");
        driver.get("https://www.loreal.com/en/");

        LorealHomePage home = new LorealHomePage(driver);

        // Wait for header and handle cookies
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        // Validate header/logo presence
        log.info("Asserting that L'Oréal logo is displayed.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible in the header.");

        // Open hamburger (main) navigation menu
        log.info("Opening the hamburger (main) menu.");
        home.openMainMenu();

        // Click 'Group' link in the opened menu and assert it expands (as per Browser-use flow)
        log.info("Clicking the 'Group' link and verifying expansion (aria-expanded=true).");
        boolean expanded = home.clickGroupRootAndWaitExpanded();
        Assert.assertTrue(expanded, "Expected 'Group' root navigation item to expand after click.");
    }
}

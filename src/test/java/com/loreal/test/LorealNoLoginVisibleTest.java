package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealNoLoginVisibleTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealNoLoginVisibleTest.class.getName());

    @Test
    public void testLorealHome_NoLoginFound_SearchShowsNoLoginLinks() {
        // Navigate to L'Oréal home (override BaseTest default URL)
        log.info("Navigating to L'Oréal home page");
        driver.get("https://www.loreal.com/en/");

        LorealHomePage home = new LorealHomePage(driver);

        // Wait for header and handle cookie banner
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        // Validate logo visible
        log.info("Asserting that L'Oréal logo is displayed.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible in header.");

        // Open main navigation and ensure there is no login/sign-in option visible
        log.info("Opening main navigation and checking for absence of Login/Sign in options.");
        home.openMainMenu();
        Assert.assertFalse(home.isLoginOptionPresent(),
                "Did not expect Login/Sign in options to be visible in main navigation.");

        // Perform a site search for "login" and validate that search results do not expose obvious login buttons/links
        log.info("Performing site search for 'login' and validating absence of actionable login links.");
        LorealSearchPage searchPage = home.search("login");
        searchPage.waitForPage();

        boolean hasLoginCtas = searchPage.hasLoginLinksOrButtons();
        log.info("Presence of Login-like links/buttons on search page: " + hasLoginCtas);

        // Align assertions with Browser-use results where no login page could be located and CAPTCHAs blocked Google path
        Assert.assertFalse(hasLoginCtas,
                "Expected no actionable 'Login/Sign in' links/buttons on L'Oréal site search results for 'login'.");
    }
}

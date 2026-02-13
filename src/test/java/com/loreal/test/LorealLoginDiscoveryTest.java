package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealLoginDiscoveryTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealLoginDiscoveryTest.class.getName());

    @Test
    public void searchForLoginOnLorealWithoutGoogleCaptcha() {
        // Navigate directly to L'Oréal to avoid Google CAPTCHA loop observed in browser-use results
        log.info("Navigating directly to L'Oréal homepage to avoid Google CAPTCHA.");
        driver.navigate().to("https://www.loreal.com/");

        LorealHomePage home = new LorealHomePage(driver);

        log.info("Waiting for L'Oréal home header.");
        home.waitForHomeHeader();

        log.info("Attempting to accept cookies if present.");
        home.acceptCookiesIfPresent();

        log.info("Validating logo is displayed on the header.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible on the homepage header.");

        // Optional: Open main menu to emulate part of navigation steps
        log.info("Opening main navigation menu (burger).");
        home.openMainMenu();

        // Perform site search for "login" using POM locators derived from browser-use JSON (xpath prioritized).
        final String query = "login";
        log.info("Performing in-site search for: " + query);
        LorealSearchPage searchPage = home.search(query);

        log.info("Waiting for search results page to load.");
        searchPage.waitForPage();

        // Validate that the search results page is for the intended query and not a Google CAPTCHA page
        log.info("Asserting search page state and that no Google CAPTCHA page was encountered.");
        Assert.assertFalse(driver.getCurrentUrl().contains("https://www.google.com/sorry/index"),
                "Unexpected Google CAPTCHA page detected; test should avoid external CAPTCHA by direct navigation.");

        // Prefer URL validation if site exposes query parameter; otherwise presence of body has been waited in POM
        Assert.assertTrue(searchPage.isOnSearchPageFor(query) || driver.getCurrentUrl().toLowerCase().contains("/search"),
                "Search page did not navigate correctly for query: " + query);

        // Validate presence of any login-like links/buttons in the search results (best-effort, robust to content)
        log.info("Checking for any login/sign-in related links/buttons in the search results.");
        boolean hasLoginCtas = searchPage.hasLoginLinksOrButtons();
        log.info("Login-like links/buttons present: " + hasLoginCtas);

        // This assertion aligns with the goal of discovering login entry points internally, avoiding CAPTCHA issues.
        Assert.assertTrue(hasLoginCtas,
                "Expected at least one login/sign-in related link/button in search results for query: " + query);
    }
}

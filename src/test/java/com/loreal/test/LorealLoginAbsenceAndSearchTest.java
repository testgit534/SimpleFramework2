package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealLoginAbsenceAndSearchTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealLoginAbsenceAndSearchTest.class.getName());

    @Test
    public void verifyNoLoginOptionAndSearchNoResults() {
        // Navigate to L'Oréal Global English homepage
        driver.get("https://www.loreal.com/en/");
        LorealHomePage home = new LorealHomePage(driver);

        // Accept cookies if present and ensure header is ready
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        // Validate logo is visible
        log.info("Asserting that the L'Oréal logo is displayed on the homepage.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible in the header.");

        // Assert no Login/Sign in option on initial header/body
        log.info("Asserting there is no Login/Sign in option on the page.");
        Assert.assertFalse(home.isLoginOptionPresent(), "Login/Sign in option should not be present on homepage.");

        // Open the main navigation menu (burger)
        home.openMainMenu();

        // Assert still no Login/Sign in option after opening the menu
        log.info("Asserting there is still no Login/Sign in option after opening the main menu.");
        Assert.assertFalse(home.isLoginOptionPresent(), "Login/Sign in option should not be present even after opening main menu.");

        // Use the homepage search to look for 'login'
        log.info("Searching site for the term 'login'.");
        LorealSearchPage searchPage = home.searchFor("login");

        // Validate navigation to search page for 'login'
        log.info("Asserting user is on the search results page for 'login'.");
        Assert.assertTrue(searchPage.isOnSearchPageFor("login"),
                "Expected URL to contain /search/?q=login but was: " + driver.getCurrentUrl());

        // Validate that there are no results for 'login' (as per Browser-use outcome)
        log.info("Asserting that search results for 'login' are empty or show 'No results' message.");
        boolean noResultsBanner = searchPage.isNoResultsMessageDisplayed();
        int resultsCount = searchPage.getVisibleResultsCount();
        log.info("No-results banner displayed: " + noResultsBanner + ", visible results count: " + resultsCount);

        Assert.assertTrue(noResultsBanner || resultsCount == 0,
                "Expected no search results for 'login', but found: " + resultsCount);
    }
}

package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealSearchE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealSearchE2ETest.class.getName());

    @Test
    public void testSearchFunctionalityOnLorealSite() {
        // Navigate to L'Oréal global EN home
        log.info("Navigating to L'Oréal website (EN).");
        driver.get("https://www.loreal.com/en/");

        LorealHomePage home = new LorealHomePage(driver);
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        log.info("Verifying header logo is displayed.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal header logo to be visible.");

        log.info("Optionally opening the main navigation (burger) to mirror user flow.");
        try {
            home.openMainMenu();
        } catch (Exception e) {
            log.info("Main menu could not be opened or is not applicable for current layout. Continuing.");
        }

        log.info("Asserting that Login/Sign-in option is not present on corporate site.");
        Assert.assertFalse(home.isLoginOptionPresent(), "Did not expect Login/Sign-in control on corporate homepage.");

        // Perform search using the homepage search box
        String query = "test search";
        LorealSearchPage searchPage = new LorealSearchPage(driver);
        log.info("Waiting for search input to be available.");
        searchPage.waitForSearchInput();

        log.info("Typing query and submitting: " + query);
        searchPage.search(query);

        log.info("Waiting for search results page to load.");
        searchPage.waitForResults();

        String currentUrl = driver.getCurrentUrl();
        String title = searchPage.getPageTitle();
        log.info("Asserting search results page state. URL: " + currentUrl + " | Title: " + title);

        Assert.assertTrue(currentUrl.contains("/search"),
                "Expected URL to contain '/search' but got: " + currentUrl);

        // Validate query is reflected in URL (handle + or %20 encodings)
        Assert.assertTrue(
                currentUrl.toLowerCase().contains("q=test+search") ||
                currentUrl.toLowerCase().contains("q=test%20search") ||
                currentUrl.toLowerCase().contains("q=test") && currentUrl.toLowerCase().contains("search"),
                "Expected URL to contain query parameter for 'test search' but got: " + currentUrl
        );

        Assert.assertTrue(title.toLowerCase().contains("search"),
                "Expected page title to contain 'Search' but got: " + title);
    }
}

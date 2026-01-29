package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import com.loreal.pages.LorealSearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealSearchE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealSearchE2ETest.class.getName());

    @Test
    public void verifySearchFlowAndAbsenceOfLoginOption() {
        // Navigate to L'Oréal site (BaseTest opens a different URL by default)
        log.info("Navigating to L'Oréal global English homepage.");
        driver.get("https://www.loreal.com/en/");

        // Initialize Home Page and handle header + cookies
        LorealHomePage home = new LorealHomePage(driver);
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        // Validate header logo present
        log.info("Asserting that the L'Oréal logo is displayed.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be displayed on the homepage header.");

        // Open main menu and assert no Login/Sign in option is present (as per Browser-use findings)
        log.info("Opening main navigation menu to verify absence of Login/Sign in option.");
        home.openMainMenu();
        Assert.assertFalse(home.isLoginOptionPresent(),
                "Login/Sign in option should not be present on the L'Oréal main corporate site.");

        // Perform search using the same query from Browser-use: "test search"
        final String query = "test search";
        log.info("Performing search with query: " + query);
        home.performSearch(query);

        // Validate search results page
        LorealSearchResultsPage results = new LorealSearchResultsPage(driver);
        results.waitForPage(query);

        // Title should indicate search context
        log.info("Validating that the search results page title indicates a search.");
        Assert.assertTrue(results.titleIndicatesSearch(), "Search results page title should contain 'Search'.");

        // Either results appear or an empty/no results message is displayed (Browser-use indicated vague query)
        boolean hasResults = results.hasAnyResults();
        boolean hasEmptyState = results.hasNoResultsMessage();
        log.info("Search results presence: " + hasResults + ", empty state present: " + hasEmptyState);

        Assert.assertTrue(hasResults || hasEmptyState,
                "Expected either visible search results or a visible 'no results' message for the query.");
    }
}

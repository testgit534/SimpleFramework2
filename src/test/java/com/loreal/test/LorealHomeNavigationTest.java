package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealHomeNavigationTest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealHomeNavigationTest.class.getName());

    @Test
    public void verifyHeaderMenuGroupAndNoLoginOption() {
        // Navigate to L'Oréal site (override BaseTest default URL)
        log.info("Navigating to L'Oréal global site.");
        driver.get("https://www.loreal.com/en/");

        LorealHomePage home = new LorealHomePage(driver);

        // Wait for header and handle cookies
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        // Basic header validations
        log.info("Asserting that the header logo is displayed.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be visible in the header.");

        // Open main navigation via burger and validate open state
        home.openMainMenu();
        log.info("Asserting that the main navigation is open after clicking burger menu.");
        Assert.assertTrue(home.isMainNavigationOpen(), "Main navigation should be open but was not.");

        // Click "Group" navigation item and verify expansion (aria-expanded=true)
        home.clickGroupNav();
        log.info("Asserting that the 'Group' navigation item is expanded.");
        Assert.assertTrue(home.isGroupNavExpanded(), "'Group' navigation item should be expanded after click.");

        // Align with Browser-use result: no clear login option was found; assert none present
        boolean loginPresent = home.isLoginOptionPresent();
        log.info("Asserting that no explicit Login/Sign in option is present.");
        Assert.assertFalse(loginPresent, "Login/Sign in option should not be present on L'Oréal corporate navigation.");

        // Validate we remain on the home locale URL and page title contains brand marker
        String currentUrl = driver.getCurrentUrl();
        String title = driver.getTitle();
        log.info("Current URL: " + currentUrl);
        log.info("Page Title: " + title);

        Assert.assertTrue(currentUrl.contains("/en/"), "Expected to remain on the /en/ home URL after expanding 'Group'.");

        // Normalize quotes in title and assert presence of expected brand phrase
        String normalizedTitle = title.replace("’", "'").toLowerCase();
        Assert.assertTrue(
                normalizedTitle.contains("l'oréal") || normalizedTitle.contains("l'oreal"),
                "Page title should contain L'Oréal brand name."
        );
        Assert.assertTrue(
                normalizedTitle.contains("world leader in beauty"),
                "Page title should indicate 'world leader in beauty'."
        );
    }
}

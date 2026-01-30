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
    public void verifyLorealHasNoPublicLoginAndSearchShowsNoLoginLinks() {
        log.info("Navigating directly to L'Oréal homepage as per Browser-use alternative approach.");
        driver.navigate().to("https://www.loreal.com/en/");

        LorealHomePage home = new LorealHomePage(driver);
        home.waitForHomeHeader();
        home.acceptCookiesIfPresent();

        log.info("Asserting L'Oréal logo is visible in the header.");
        Assert.assertTrue(home.isLogoDisplayed(), "Expected L'Oréal logo to be displayed on home page.");

        log.info("Opening main navigation (burger) and validating absence of login/sign-in.");
        home.openMainMenu();
        Assert.assertFalse(home.isLoginOptionPresent(),
                "Did not expect to find a public Login/Sign in entry in the main navigation per Browser-use results.");

        log.info("Performing site search for 'login' as attempted in Browser-use flow.");
        LorealSearchPage searchPage = home.search("login");
        searchPage.waitForPage();

        log.info("Asserting we landed on the L'Oréal search page for 'login'.");
        Assert.assertTrue(searchPage.isOnSearchPageFor("login"),
                "Expected to be on L'Oréal search results for 'login'.");

        log.info("Verifying there are no actionable login/sign-in links on the search results page (aligns with 'no login options found').");
        Assert.assertFalse(searchPage.hasLoginLinksOrButtons(),
                "Did not expect any actionable Login/Sign in links on the search results page.");

        log.info("Test completed: L'Oréal site shows no public Login/Sign in, and search for 'login' yields no actionable login links.");
    }
}

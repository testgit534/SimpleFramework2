package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.LorealHomePage;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class LorealHomeE2ETest extends BaseTest {

    private static final Logger log = Logger.getLogger(LorealHomeE2ETest.class.getName());

    @Test
    public void verifyLorealLogoAndAbsenceOfLoginOption() {
        // Step 1: Navigate to L'Oréal website (override BaseTest initial URL)
        String url = "https://www.loreal.com/en/";
        log.info("Navigating to: " + url);
        Reporter.log("Navigating to: " + url, true);
        driver.get(url);

        LorealHomePage home = new LorealHomePage(driver);

        // Step 2: Wait for header (page ready)
        home.waitForHomeHeader();
        Reporter.log("Home header is visible.", true);

        // Step 3: Handle cookie banner explicitly (per Browser-use JSON)
        home.acceptCookiesIfPresent();
        Reporter.log("Cookie consent handled if present.", true);

        // Step 4: Validate logo is displayed (as per Browser-use results)
        boolean logoVisible = home.isLogoDisplayed();
        Reporter.log("Logo displayed: " + logoVisible, true);
        Assert.assertTrue(logoVisible, "Expected L'Oréal logo to be visible on the home page.");

        // Step 5: Open main navigation (burger menu)
        home.openMainMenu();
        Reporter.log("Main navigation opened.", true);

        // Step 6: Assert no login/sign-in option exists (aligning with Browser-use conclusion)
        boolean loginPresent = home.isLoginOptionPresent();
        Reporter.log("Login/Sign in option present: " + loginPresent, true);
        Assert.assertFalse(loginPresent, "No login/sign-in option should be present on loreal.com corporate site.");

        Reporter.log("E2E validation completed: Logo verified and confirmed absence of login functionality.", true);
    }
}

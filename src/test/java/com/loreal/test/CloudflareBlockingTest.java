package com.loreal.test;

import base.BaseTest;
import com.loreal.pages.CloudflareChallengePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.logging.Logger;

public class CloudflareBlockingTest extends BaseTest {

    private static final Logger log = Logger.getLogger(CloudflareBlockingTest.class.getName());

    @Test
    public void verifyCloudflareBlocksLorealPartnerLogin() {
        String targetLoginUrl = "https://fr.lorealpartnershop.com/fr/login/";
        log.info("Navigating directly to L'Oréal Partner Shop login: " + targetLoginUrl);
        driver.get(targetLoginUrl);

        CloudflareChallengePage challengePage = new CloudflareChallengePage(driver);
        log.info("Waiting for potential Cloudflare challenge to appear.");
        boolean challengeAppeared = challengePage.waitForChallengeOrTimeout();

        log.info("Asserting Cloudflare challenge presence aligns with observed browser-use results.");
        Assert.assertTrue(challengeAppeared,
                "Expected Cloudflare 'Just a moment...' challenge to appear, but it did not.");

        log.info("Validating that Cloudflare vendor link (from JSON locator) is visible, if available.");
        boolean vendorLinkVisible = challengePage.isVendorLinkVisible();
        Assert.assertTrue(vendorLinkVisible || driver.getTitle().toLowerCase().contains("just a moment"),
                "Cloudflare challenge indicators not found (vendor link or 'Just a moment...' title).");

        log.info("Final assertion: We confirm that the login page is not reachable due to Cloudflare verification.");
        Assert.assertTrue(challengeAppeared, "Login page should be blocked by Cloudflare per execution results.");
    }
}
